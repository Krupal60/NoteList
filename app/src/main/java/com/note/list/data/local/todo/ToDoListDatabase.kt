package com.note.list.data.local.todo

import androidx.room.Database
import androidx.room.RoomDatabase

@Database( entities = [ToDoList::class],
    version = 1,
    exportSchema = false)
abstract class ToDoListDatabase : RoomDatabase() {
    abstract fun toDoDao() : ToDoDao
}