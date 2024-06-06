package com.note.list.domain.todo

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getToDoListByNotDone(): Flow<Result<List<ToDo>>>
    fun getToDoListByDone(): Flow<Result<List<ToDo>>>
    fun getToDoListDetail(id: Int): Flow<Result<ToDo>>
    suspend fun upsert(toDo: ToDo)
    suspend fun delete(toDo: ToDo)
}