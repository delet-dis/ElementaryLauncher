package com.delet_dis.elementarylauncher.data.database.entities

import android.graphics.drawable.Drawable
import androidx.room.*
import com.delet_dis.elementarylauncher.data.database.EntitiesParent
import com.delet_dis.elementarylauncher.data.models.ActionType

@Entity(indices = [Index(value = ["position"], unique = true)])
data class SettingsAction(

    var actionName: String? = null,

    @ColumnInfo(name = "position")
    override var position: Int? = null,

    var label: String? = null,

    @Ignore
    var icon: Drawable? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @Ignore
    override var entityType: Any = ActionType.SETTINGS_ACTION
) : EntitiesParent
