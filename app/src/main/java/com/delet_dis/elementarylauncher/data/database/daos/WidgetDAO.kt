package com.delet_dis.elementarylauncher.data.database.daos

import androidx.room.*
import com.delet_dis.elementarylauncher.data.database.entities.Widget
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetDAO {
    @Query("SELECT * FROM widget")
    fun getAllWidgetsAsList(): List<Widget>

    @Query("SELECT * FROM widget")
    fun getAllWidgetsAsFlow(): Flow<List<Widget>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(widget: Widget)

    @Update
    suspend fun update(widget: Widget)

    @Delete
    suspend fun delete(widget: Widget)

    @Query("DELETE FROM widget WHERE position = :position")
    suspend fun removeWidgetByPosition(position: Int)
}