package com.note.list.viewmodel

import androidx.lifecycle.ViewModel
import com.note.list.data.repository.note.NoteRepositoryImpl
import com.note.list.domain.note.Note
import com.note.list.domain.todo.OnToDoAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(repository: NoteRepositoryImpl) : ViewModel() {
    val notes: Flow<Result<List<Note>>> = repository.getNotes()
}