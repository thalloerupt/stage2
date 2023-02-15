package com.thallo.stage.database.bookmark

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Bookmark(
    @field:ColumnInfo(name = "url_info") var url: String, @field:ColumnInfo(
        name = "title_info"
    ) var title: String, @field:ColumnInfo(name = "file_name") var file: String, @field:ColumnInfo(
        name = "show_info"
    ) var show: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "mix")
    var mix: String = url + title

}