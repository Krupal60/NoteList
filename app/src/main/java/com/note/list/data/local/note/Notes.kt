package com.note.list.data.local.note

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")

data class Notes(
    val title : String,
    val description : String,
    val lastUpdated  :Long,
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0
)
