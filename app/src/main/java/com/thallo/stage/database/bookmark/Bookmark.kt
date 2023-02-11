package com.thallo.stage.database.bookmark

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT) val title: String?,
    @ColumnInfo(name = "url", typeAffinity = ColumnInfo.TEXT) val url: String?,
    @ColumnInfo(name = "category", typeAffinity = ColumnInfo.TEXT) val category: String?,

    )