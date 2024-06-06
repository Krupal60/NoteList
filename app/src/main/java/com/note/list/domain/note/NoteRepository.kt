package com.note.list.domain.note

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<Result<List<Note>>>
    fun getNotesDetail(id: Int): Flow<Result<Note>>
    suspend fun upsert(note: Note)
    suspend fun delete(note: Note)
}