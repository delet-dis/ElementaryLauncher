package com.delet_dis.elementarylauncher.data.database.daos

import androidx.room.*
import com.delet_dis.elementarylauncher.data.database.entities.ContactSMS
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactSMSDAO {

    @Query("SELECT * FROM contactsms")
    fun getAllContactSMSAsList(): List<ContactSMS>

    @Query("SELECT * FROM contactsms")
    fun getAllContactSMSAsFlow(): Flow<List<ContactSMS>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactSMS: ContactSMS)

    @Update
    suspend fun update(contactSMS: ContactSMS)

    @Delete
    suspend fun delete(contactSMS: ContactSMS)

    @Query("DELETE FROM contactsms WHERE position = :position")
    suspend fun removeContactSMSByPosition(position: Int)
}
