package com.delet_dis.elementarylauncher.data.database.daos

import androidx.room.*
import com.delet_dis.elementarylauncher.data.database.entities.App
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDAO {
    @Query("SELECT * FROM app")
    fun getAllAppsAsList(): List<App>

    @Query("SELECT * FROM app")
    fun getAllAppsAsFlow(): Flow<List<App>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: App)

    @Update
    suspend fun update(app: App)

    @Delete
    suspend fun delete(app: App)

    @Query("DELETE FROM app WHERE position = :position")
    suspend fun removeAppByPosition(position: Int)
}