package com.note.list.data.local.todo

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Immutable
data class ToDoList(
    val description : String,
    val lastUpdated : Long,
    val isDone : Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id :Int = 0

)
