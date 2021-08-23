package com.delet_dis.elementarylauncher.data.database.daos

import androidx.room.*
import com.delet_dis.elementarylauncher.data.database.entities.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDAO {

    @Query("SELECT * FROM contact")
    fun getAllContactsAsList(): List<Contact>

    @Query("SELECT * FROM contact")
    fun getAllContactsAsFlow(): Flow<List<Contact>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("DELETE FROM contact WHERE position = :position")
    suspend fun removeContactByPosition(position: Int)
}
