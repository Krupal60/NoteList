package com.note.list.data.repository.note

import com.note.list.data.local.note.NoteDao
import com.note.list.data.local.note.Notes
import com.note.list.domain.note.Note
import com.note.list.domain.note.NoteRepository
import com.note.list.domain.note.toNote
import com.note.list.domain.note.toNotes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
   private val dao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<Result<List<Note>>> {
        return  dao.getNotes().map { notes ->
            try {
                Result.success(notes.map { it.toNote() })
            }catch (e:Exception){
                Result.failure(e)
            }
        }
    }

    override fun getNotesDetail(id: Int): Flow<Result<Note>> {
        return  dao.getNoteDetail(id).map { note ->
            try {
                Result.success(note.toNote())
            }catch (e:Exception){
                Result.failure(e)
            }
        }
    }

    override suspend fun upsert(note: Note) {
        dao.insertNote(note.toNotes())
    }

    override suspend fun delete(note: Note) {
        dao.deleteNotes(note.toNotes())
    }
}