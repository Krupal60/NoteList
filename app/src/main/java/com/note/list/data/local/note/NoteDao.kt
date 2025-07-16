package com.note.list.data.local.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun insertNote( vararg notes: Notes)

    @Query("SELECT * FROM notes ORDER BY lastUpdated DESC")
    fun getNotes(): Flow<List<Notes>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteDetail(id: Int): Flow<Notes?>

    @Delete
    suspend fun deleteNotes(vararg notes: Notes)

}