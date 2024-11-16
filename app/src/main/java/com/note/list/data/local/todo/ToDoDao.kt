package com.note.list.data.local.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query(" SELECT * FROM  ToDoList WHERE isDone = 0" )
    fun getToDoListByNotDone() : Flow<List<ToDoList>>

    @Query(" SELECT * FROM  ToDoList WHERE isDone = 1 ORDER BY lastUpdated DESC" )
    fun getToDoListByDone() : Flow<List<ToDoList>>

    @Query(" SELECT * FROM  ToDoList WHERE id = :id" )
    suspend fun getToDoListDetail(id: Int): ToDoList

    @Upsert
    suspend fun upsert(vararg toDoList : ToDoList)

    @Delete
    suspend fun delete(vararg toDoList : ToDoList)
}