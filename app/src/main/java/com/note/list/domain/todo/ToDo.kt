package com.note.list.domain.todo

import androidx.compose.runtime.Stable
import com.note.list.data.local.todo.ToDoList

@Stable
data class ToDo(
    val description: String,
    val lastUpdated: Long,
    val isDone : Boolean = false,
    val id: Int = 0
)

@Stable
fun ToDoList.toToDo(): ToDo {
    return ToDo(
    description = this.description,
    lastUpdated = this.lastUpdated,
        isDone = this.isDone,
    id = this.id
    )
}

@Stable
fun ToDo.toToDoList(): ToDoList {
    return ToDoList(
        description = this.description,
        lastUpdated = this.lastUpdated,
        isDone = this.isDone,
        id = this.id
    )
}
