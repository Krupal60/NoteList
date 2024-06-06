package com.note.list.data.local.note

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Notes::class], version = 1 , exportSchema = false)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun  noteDao() : NoteDao
}