package com.delet_dis.elementarylauncher.data.database.daos

import androidx.room.*
import com.delet_dis.elementarylauncher.data.database.entities.SettingsAction
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsActionDAO {

    @Query("SELECT * FROM settingsaction")
    fun getAllSettingsActionsAsList(): List<SettingsAction>

    @Query("SELECT * FROM settingsaction")
    fun getAllSettingsActionsAsFlow(): Flow<List<SettingsAction>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settingsAction: SettingsAction)

    @Update
    suspend fun update(settingsAction: SettingsAction)

    @Delete
    suspend fun delete(settingsAction: SettingsAction)

    @Query("DELETE FROM settingsaction WHERE position = :position")
    suspend fun removeSettingsActionByPosition(position: Int)
}
