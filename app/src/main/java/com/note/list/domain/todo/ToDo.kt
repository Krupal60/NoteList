package com.note.list.domain.todo

import com.note.list.data.local.todo.ToDoList

data class ToDo(
    val description: String,
    val lastUpdated: Long,
    val isDone : Boolean = false,
    val id: Int = 0
)

fun ToDoList.toToDo(): ToDo {
    return ToDo(
    description = this.description,
    lastUpdated = this.lastUpdated,
        isDone = this.isDone,
    id = this.id
    )
}

fun ToDo.toToDoList(): ToDoList {
    return ToDoList(
        description = this.description,
        lastUpdated = this.lastUpdated,
        isDone = this.isDone,
        id = this.id
    )
}
