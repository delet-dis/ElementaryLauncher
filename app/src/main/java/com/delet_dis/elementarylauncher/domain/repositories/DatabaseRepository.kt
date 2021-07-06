package com.delet_dis.elementarylauncher.domain.repositories

import android.content.Context
import androidx.core.content.ContextCompat
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.database.EntitiesParent
import com.delet_dis.elementarylauncher.data.database.daos.*
import com.delet_dis.elementarylauncher.data.database.entities.*
import com.delet_dis.elementarylauncher.data.mappers.mapEntityToCard
import com.delet_dis.elementarylauncher.data.models.ActionType
import com.delet_dis.elementarylauncher.data.models.Card
import com.delet_dis.elementarylauncher.domain.extensions.concatenate
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
class DatabaseRepository @Inject constructor(
    private val appDAO: AppDAO,
    private val contactDAO: ContactDAO,
    private val contactCallDAO: ContactCallDAO,
    private val contactSMSDAO: ContactSMSDAO,
    private val settingsActionDAO: SettingsActionDAO,
    private val widgetDAO: WidgetDAO,
) {
    private fun getAllDatabaseRecordingsAsEntitiesParentList(): List<EntitiesParent> {
        return concatenate(
            appDAO.getAllAppsAsList(),
            contactDAO.getAllContactsAsList(),
            contactCallDAO.getAllContactCallsAsList(),
            contactSMSDAO.getAllContactSMSAsList(),
            settingsActionDAO.getAllSettingsActionsAsList(),
            widgetDAO.getAllWidgetsAsList()
        )
    }

    fun getAllDatabaseRecordingsAsEntitiesParentListFlow(): Flow<List<EntitiesParent>> =
        combine(
            appDAO.getAllAppsAsFlow(),
            contactDAO.getAllContactsAsFlow(),
            contactCallDAO.getAllContactCallsAsFlow(),
            contactSMSDAO.getAllContactSMSAsFlow(),
            settingsActionDAO.getAllSettingsActionsAsFlow(),
            widgetDAO.getAllWidgetsAsFlow()
        ) {
            it
                .toList()
                .flatten()
                .toMutableList()
        }

    fun getAllDatabaseRecordingsAsCards(@ApplicationContext context: Context): Flow<List<Card>> =
        combine(
            appDAO.getAllAppsAsFlow(),
            contactDAO.getAllContactsAsFlow(),
            contactCallDAO.getAllContactCallsAsFlow(),
            contactSMSDAO.getAllContactSMSAsFlow(),
            settingsActionDAO.getAllSettingsActionsAsFlow(),
            widgetDAO.getAllWidgetsAsFlow()
        ) {
            val processingList = it
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

    fun getNonEmptyDatabaseRecordingsAsCards(@ApplicationContext context: Context): Flow<Array<Card?>> =
        combine(
            appDAO.getAllAppsAsFlow(),
            contactDAO.getAllContactsAsFlow(),
            contactCallDAO.getAllContactCallsAsFlow(),
            contactSMSDAO.getAllContactSMSAsFlow(),
            settingsActionDAO.getAllSettingsActionsAsFlow(),
            widgetDAO.getAllWidgetsAsFlow()
        ) {
            val processingList = it
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
            is App -> appDAO.insert(entity)
            is Contact -> contactDAO.insert(entity)
            is ContactCall -> contactCallDAO.insert(entity)
            is ContactSMS -> contactSMSDAO.insert(entity)
            is SettingsAction -> settingsActionDAO.insert(entity)
            is Widget -> widgetDAO.insert(entity)
        }

    }


    suspend fun deleteAtPosition(position: Int) {
        getAllDatabaseRecordingsAsEntitiesParentList().forEach { entitiesParent ->

            when (entitiesParent.entityType) {
                ActionType.APP ->
                    appDAO.removeAppByPosition(position)

                ActionType.CONTACT ->
                    contactDAO.removeContactByPosition(position)

                ActionType.CONTACT_CALL ->
                    contactCallDAO.removeContactCallByPosition(position)

                ActionType.CONTACT_SMS ->
                    contactSMSDAO.removeContactSMSByPosition(position)

                ActionType.SETTINGS_ACTION ->
                    settingsActionDAO.removeSettingsActionByPosition(position)

                ActionType.WIDGET ->
                    widgetDAO.removeWidgetByPosition(position)
            }
        }
    }
}
