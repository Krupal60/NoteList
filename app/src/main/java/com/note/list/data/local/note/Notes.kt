package com.note.list.data.local.note

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
@Stable
data class Notes(
    val title : String,
    val description : String,
    val lastUpdated  :Long,
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0
)
