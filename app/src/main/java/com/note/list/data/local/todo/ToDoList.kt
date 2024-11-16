package com.note.list.data.local.todo

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Stable
data class ToDoList(
    val description : String,
    val lastUpdated : Long,
    val isDone : Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id :Int = 0

)
