package com.note.list.domain.todo

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getToDoListByNotDone(): Flow<Result<List<ToDo>>>
    fun getToDoListByDone(): Flow<Result<List<ToDo>>>
    suspend fun getToDoListDetail(id: Int): ToDo
    suspend fun upsert(toDo: ToDo)
    suspend fun delete(toDo: ToDo)
}