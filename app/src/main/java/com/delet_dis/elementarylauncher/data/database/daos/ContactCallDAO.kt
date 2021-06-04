package com.delet_dis.elementarylauncher.data.database.daos

import androidx.room.*
import com.delet_dis.elementarylauncher.data.database.entities.ContactCall
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactCallDAO {
    @Query("SELECT * FROM contactcall")
    fun getAllContactCallsAsList(): List<ContactCall>

    @Query("SELECT * FROM contactcall")
    fun getAllContactCallsAsFlow(): Flow<List<ContactCall>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactCall: ContactCall)

    @Update
    suspend fun update(contactCall: ContactCall)

    @Delete
    suspend fun delete(contactCall: ContactCall)

    @Query("DELETE FROM contactcall WHERE position = :position")
    suspend fun removeContactCallByPosition(position: Int)
}