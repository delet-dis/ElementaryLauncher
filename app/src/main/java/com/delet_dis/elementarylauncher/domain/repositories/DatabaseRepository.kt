package com.delet_dis.elementarylauncher.domain.repositories

import android.content.Context
import androidx.core.content.ContextCompat
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.database.EntitiesParent
import com.delet_dis.elementarylauncher.data.database.ShortcutsDatabase
import com.delet_dis.elementarylauncher.data.database.daos.*
import com.delet_dis.elementarylauncher.data.database.entities.*
import com.delet_dis.elementarylauncher.data.mappers.mapEntityToCard
import com.delet_dis.elementarylauncher.data.models.ActionType
import com.delet_dis.elementarylauncher.data.models.Card
import com.delet_dis.elementarylauncher.domain.extensions.concatenate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class DatabaseRepository(val context: Context) {

    companion object {
        private var appDAO: AppDAO? = null
        fun getAppDao(context: Context): AppDAO {
            if (appDAO == null) {
                synchronized(AppDAO::class) {
                    appDAO = ShortcutsDatabase.getAppDatabase(context).appDao()
                }
            }
            return appDAO!!
        }

        private var contactCallDAO: ContactCallDAO? = null
        fun getContactCallDao(context: Context): ContactCallDAO {
            if (contactCallDAO == null) {
                synchronized(AppDAO::class) {
                    contactCallDAO = ShortcutsDatabase.getAppDatabase(context).contactCallDao()
                }
            }
            return contactCallDAO!!
        }

        private var contactSMSDAO: ContactSMSDAO? = null
        fun getContactSMSDao(context: Context): ContactSMSDAO {
            if (contactSMSDAO == null) {
                synchronized(AppDAO::class) {
                    contactSMSDAO = ShortcutsDatabase.getAppDatabase(context).contactSmsDao()
                }
            }
            return contactSMSDAO!!
        }

        private var contactDAO: ContactDAO? = null
        fun getContactDao(context: Context): ContactDAO {
            if (contactDAO == null) {
                synchronized(AppDAO::class) {
                    contactDAO = ShortcutsDatabase.getAppDatabase(context).contactDao()
                }
            }
            return contactDAO!!
        }

        private var settingsActionDAO: SettingsActionDAO? = null
        fun getSettingsActionDao(context: Context): SettingsActionDAO {
            if (settingsActionDAO == null) {
                synchronized(AppDAO::class) {
                    settingsActionDAO =
                        ShortcutsDatabase.getAppDatabase(context).settingsActionDao()
                }
            }
            return settingsActionDAO!!
        }

        private var widgetDAO: WidgetDAO? = null
        fun getWidgetDao(context: Context): WidgetDAO {
            if (widgetDAO == null) {
                synchronized(AppDAO::class) {
                    widgetDAO =
                        ShortcutsDatabase.getAppDatabase(context).widgetDao()
                }
            }
            return widgetDAO!!
        }
    }

    private fun getAllDatabaseRecordingsAsEntitiesParentList(): List<EntitiesParent> {
        return concatenate(
            getAppDao(context).getAllAppsAsList(),
            getContactDao(context).getAllContactsAsList(),
            getContactCallDao(context).getAllContactCallsAsList(),
            getContactSMSDao(context).getAllContactSMSAsList(),
            getSettingsActionDao(context).getAllSettingsActionsAsList(),
            getWidgetDao(context).getAllWidgetsAsList()
        )
    }

    fun getAllDatabaseRecordingsAsEntitiesParentListFlow(): Flow<List<EntitiesParent>> =
        combine(
            getAppDao(context).getAllAppsAsFlow(),
            getContactDao(context).getAllContactsAsFlow(),
            getContactCallDao(context).getAllContactCallsAsFlow(),
            getContactSMSDao(context).getAllContactSMSAsFlow(),
            getSettingsActionDao(context).getAllSettingsActionsAsFlow(),
            getWidgetDao(context).getAllWidgetsAsFlow()
        ) { results ->

            results
                .toList()
                .flatten()
                .toMutableList()
        }

    fun getAllDatabaseRecordingsAsCards(): Flow<List<Card>> = combine(
        getAppDao(context).getAllAppsAsFlow(),
        getContactDao(context).getAllContactsAsFlow(),
        getContactCallDao(context).getAllContactCallsAsFlow(),
        getContactSMSDao(context).getAllContactSMSAsFlow(),
        getSettingsActionDao(context).getAllSettingsActionsAsFlow(),
        getWidgetDao(context).getAllWidgetsAsFlow()
    ) { results ->

        val processingList = results
            .toList()
            .flatten()
            .map { entitiesParent -> mapEntityToCard(entitiesParent, context) }
            .toMutableList()


        for (i in 1..SharedPreferencesRepository(context).getLayoutType().numberOfRows * 2) {
            run loop@{
                processingList.forEach { card ->
                    if (card.position == i) {
                        return@loop
                    }
                }

                processingList.add(
                    Card(
                        null,
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.picking_image
                        ),
                        i
                    )
                )
            }
        }

        processingList.sortWith { o1, o2 ->
            o1.position?.let { o1Int ->
                o2.position?.let { o2Int ->
                    o1Int.compareTo(o2Int)
                }
            }!!
        }

        processingList
    }

    fun getNonEmptyDatabaseRecordingsAsCards(): Flow<Array<Card?>> = combine(
        getAppDao(context).getAllAppsAsFlow(),
        getContactDao(context).getAllContactsAsFlow(),
        getContactCallDao(context).getAllContactCallsAsFlow(),
        getContactSMSDao(context).getAllContactSMSAsFlow(),
        getSettingsActionDao(context).getAllSettingsActionsAsFlow(),
        getWidgetDao(context).getAllWidgetsAsFlow()
    ) { results ->

        val processingList = results
            .toList()
            .flatten()
            .map { entitiesParent -> mapEntityToCard(entitiesParent, context) }
            .toMutableList()


        val listToReturn = arrayOfNulls<Card>(6)

        processingList.forEach { card ->
            listToReturn[card.position!! - 1] = card
        }

        listToReturn
    }

    suspend fun insertWithOverride(
        entity: Any?,
        position: Int
    ) {
        getAllDatabaseRecordingsAsEntitiesParentList().forEach { entitiesParent ->
            if (entitiesParent.position == position) {
                deleteAtPosition(position)
            }
        }


        when (entity) {
            is App -> getAppDao(context).insert(entity)
            is Contact -> getContactDao(context).insert(entity)
            is ContactCall -> getContactCallDao(context).insert(entity)
            is ContactSMS -> getContactSMSDao(context).insert(entity)
            is SettingsAction -> getSettingsActionDao(context).insert(entity)
            is Widget -> getWidgetDao(context).insert(entity)
        }

    }


    suspend fun deleteAtPosition(position: Int) {
        getAllDatabaseRecordingsAsEntitiesParentList().forEach { entitiesParent ->


            when (entitiesParent.entityType) {
                ActionType.APP ->
                    getAppDao(context).removeAppByPosition(position)

                ActionType.CONTACT ->
                    getContactDao(context).removeContactByPosition(position)

                ActionType.CONTACT_CALL ->
                    getContactCallDao(context).removeContactCallByPosition(position)

                ActionType.CONTACT_SMS ->
                    getContactSMSDao(context).removeContactSMSByPosition(position)

                ActionType.SETTINGS_ACTION ->
                    getSettingsActionDao(context).removeSettingsActionByPosition(position)

                ActionType.WIDGET ->
                    getWidgetDao(context).removeWidgetByPosition(position)
            }
        }
    }
}