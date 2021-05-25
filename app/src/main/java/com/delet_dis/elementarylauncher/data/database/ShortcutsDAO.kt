package com.delet_dis.elementarylauncher.data.database

import androidx.room.*
import com.delet_dis.elementarylauncher.data.database.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortcutsDAO {

    @Query("SELECT * FROM app")
    fun getAllAppsAsList(): List<App>

    @Query("SELECT * FROM contact")
    fun getAllContactsAsList(): List<Contact>

    @Query("SELECT * FROM contactcall")
    fun getAllContactCallsAsList(): List<ContactCall>

    @Query("SELECT * FROM contactsms")
    fun getAllContactSMSAsList(): List<ContactSMS>

    @Query("SELECT * FROM settingsaction")
    fun getAllSettingsActionsAsList(): List<SettingsAction>

    @Query("SELECT * FROM shortcut")
    fun getAllShortcutsAsList(): List<Shortcut>

    @Query("SELECT * FROM widget")
    fun getAllWidgetsAsList(): List<Widget>

    @Query("SELECT * FROM app")
    fun getAllAppsAsFlow(): Flow<List<App>>

    @Query("SELECT * FROM contact")
    fun getAllContactsAsFlow(): Flow<List<Contact>>

    @Query("SELECT * FROM contactcall")
    fun getAllContactCallsAsFlow(): Flow<List<ContactCall>>

    @Query("SELECT * FROM contactsms")
    fun getAllContactSMSAsFlow(): Flow<List<ContactSMS>>

    @Query("SELECT * FROM settingsaction")
    fun getAllSettingsActionsAsFlow(): Flow<List<SettingsAction>>

    @Query("SELECT * FROM shortcut")
    fun getAllShortcutsAsFlow(): Flow<List<Shortcut>>

    @Query("SELECT * FROM widget")
    fun getAllWidgetsAsFlow(): Flow<List<Widget>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: App)

    @Update
    suspend fun update(app: App)

    @Delete
    suspend fun delete(app: App)

    @Query("DELETE FROM app WHERE position = :position")
    suspend fun removeAppByPosition(position: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("DELETE FROM contact WHERE position = :position")
    suspend fun removeContactByPosition(position: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactCall: ContactCall)

    @Update
    suspend fun update(contactCall: ContactCall)

    @Delete
    suspend fun delete(contactCall: ContactCall)

    @Query("DELETE FROM contactcall WHERE position = :position")
    suspend fun removeContactCallByPosition(position: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactSMS: ContactSMS)

    @Update
    suspend fun update(contactSMS: ContactSMS)

    @Delete
    suspend fun delete(contactSMS: ContactSMS)

    @Query("DELETE FROM contactsms WHERE position = :position")
    suspend fun removeContactSMSByPosition(position: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settingsAction: SettingsAction)

    @Update
    suspend fun update(settingsAction: SettingsAction)

    @Delete
    suspend fun delete(settingsAction: SettingsAction)

    @Query("DELETE FROM settingsaction WHERE position = :position")
    suspend fun removeSettingsActionByPosition(position: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shortcut: Shortcut)

    @Update
    suspend fun update(shortcut: Shortcut)

    @Delete
    suspend fun delete(shortcut: Shortcut)

    @Query("DELETE FROM shortcut WHERE position = :position")
    suspend fun removeShortcutByPosition(position: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(widget: Widget)

    @Update
    suspend fun update(widget: Widget)

    @Delete
    suspend fun delete(widget: Widget)

    @Query("DELETE FROM widget WHERE position = :position")
    suspend fun removeWidgetByPosition(position: Int)
}
