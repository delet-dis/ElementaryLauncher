package com.delet_dis.elementarylauncher.data.repositories

import android.content.Context
import androidx.core.content.ContextCompat
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.common.extensions.concatenate
import com.delet_dis.elementarylauncher.common.mappers.mapEntityToCard
import com.delet_dis.elementarylauncher.common.models.ActionType
import com.delet_dis.elementarylauncher.common.models.Card
import com.delet_dis.elementarylauncher.data.database.EntitiesParent
import com.delet_dis.elementarylauncher.data.database.ShortcutsDAO
import com.delet_dis.elementarylauncher.data.database.ShortcutsDatabase
import com.delet_dis.elementarylauncher.data.database.entities.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class DatabaseRepository(val context: Context) {

    companion object {
        private var INSTANCE: ShortcutsDAO? = null

        fun getShortcutsDao(context: Context): ShortcutsDAO {
            if (INSTANCE == null) {
                synchronized(ShortcutsDAO::class) {
                    INSTANCE = ShortcutsDatabase.getAppDatabase(context).shortcutsDao()
                }
            }
            return INSTANCE!!
        }
    }

    private val databaseDao = getShortcutsDao(context)

    private fun getAllDatabaseRecordingsAsEntitiesParentList(): List<EntitiesParent> {
        with(databaseDao) {
            return concatenate(
                getAllAppsAsList(),
                getAllContactsAsList(),
                getAllContactCallsAsList(),
                getAllContactSMSAsList(),
                getAllSettingsActionsAsList(),
                getAllShortcutsAsList(),
                getAllWidgetsAsList()
            )
        }
    }

    fun getAllDatabaseRecordingsAsEntitiesParentListFlow(): Flow<List<EntitiesParent>> =
        combine(
            databaseDao.getAllAppsAsFlow(),
            databaseDao.getAllContactCallsAsFlow(),
            databaseDao.getAllContactSMSAsFlow(),
            databaseDao.getAllContactsAsFlow(),
            databaseDao.getAllSettingsActionsAsFlow(),
            databaseDao.getAllShortcutsAsFlow(),
            databaseDao.getAllWidgetsAsFlow()
        ) { results ->

            results
                .toList()
                .flatten()
                .toMutableList()
        }

    fun getAllDatabaseRecordingsAsCards(): Flow<List<Card>> = combine(
        databaseDao.getAllAppsAsFlow(),
        databaseDao.getAllContactCallsAsFlow(),
        databaseDao.getAllContactSMSAsFlow(),
        databaseDao.getAllContactsAsFlow(),
        databaseDao.getAllSettingsActionsAsFlow(),
        databaseDao.getAllShortcutsAsFlow(),
        databaseDao.getAllWidgetsAsFlow()
    ) { results ->

        val processingList = results
            .toList()
            .flatten()
            .map { mapEntityToCard(it, context) }
            .toMutableList()


        for (i in 1..SharedPreferencesRepository(context).getLayoutType().numberOfRows * 2) {
            run loop@{
                processingList.forEach {
                    if (it.position == i) {
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
        databaseDao.getAllAppsAsFlow(),
        databaseDao.getAllContactCallsAsFlow(),
        databaseDao.getAllContactSMSAsFlow(),
        databaseDao.getAllContactsAsFlow(),
        databaseDao.getAllSettingsActionsAsFlow(),
        databaseDao.getAllShortcutsAsFlow(),
        databaseDao.getAllWidgetsAsFlow()
    ) { results ->

        val processingList = results
            .toList()
            .flatten()
            .map { mapEntityToCard(it, context) }
            .toMutableList()


        val listToReturn = arrayOfNulls<Card>(6)

        processingList.forEach {
            listToReturn[it.position!!-1] = it
        }

        listToReturn
    }

    suspend fun insertWithOverride(
        entity: Any?,
        position: Int
    ) {
        getAllDatabaseRecordingsAsEntitiesParentList().forEach {
            if (it.position == position) {
                deleteAtPosition(position)
            }
        }

        with(databaseDao) {
            when (entity) {
                is App -> insert(entity)
                is Contact -> insert(entity)
                is ContactCall -> insert(entity)
                is ContactSMS -> insert(entity)
                is SettingsAction -> insert(entity)
                is Shortcut -> insert(entity)
                is Widget -> insert(entity)
            }
        }
    }


    suspend fun deleteAtPosition(position: Int) {
        getAllDatabaseRecordingsAsEntitiesParentList().forEach {

            with(databaseDao) {
                when (it.entityType) {
                    ActionType.APP ->
                        removeAppByPosition(position)

                    ActionType.CONTACT ->
                        removeContactByPosition(position)

                    ActionType.CONTACT_CALL ->
                        removeContactCallByPosition(position)

                    ActionType.CONTACT_SMS ->
                        removeContactCallByPosition(position)

                    ActionType.SETTINGS_ACTION ->
                        removeSettingsActionByPosition(position)

//                    ActionType.SHORTCUT ->
//                        removeShortcutByPosition(position)

                    ActionType.WIDGET ->
                        removeWidgetByPosition(position)
                }
            }

        }
    }
}