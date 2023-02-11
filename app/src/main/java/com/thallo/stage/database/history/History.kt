package com.thallo.stage.database.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT) val title: String?,
    @ColumnInfo(name = "url", typeAffinity = ColumnInfo.TEXT) val url: String?,
    @ColumnInfo(name = "time", typeAffinity = ColumnInfo.TEXT) val time: String?,

    )