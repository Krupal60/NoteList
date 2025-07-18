package com.note.list.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.list.domain.note.Note
import com.note.list.domain.note.NoteRepository
import com.note.list.domain.upsert.OnNoteUpsertAction
import com.note.list.ui.view.screens.UpsertState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id = savedStateHandle.get<Int>("id")

    var state = MutableStateFlow(UpsertState())
        private set

    private var result: Note? = null

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            if (id != null) {
                repository.getNotesDetail(id).collect { resultData ->
                    resultData.map {
                        result = it
                        state.value = state.value.copy(
                            title = result?.title ?: "",
                            description = result?.description ?: ""
                        )
                    }
                }

            }

        }
    }


    fun onAction(onNoteUpsertAction: OnNoteUpsertAction) {
        when (onNoteUpsertAction) {
            is OnNoteUpsertAction.Delete -> deleteNote()
            is OnNoteUpsertAction.NoteUpsert -> upsertNote()
        }
    }

    private fun upsertNote() {
        val valueChanged =
            state.value.title != result?.title || state.value.description != result?.description
        if (valueChanged && state.value.title.isNotBlank() && state.value.description.isNotBlank()) {
            viewModelScope.launch {
                repository.upsert(
                    Note(
                        state.value.title.trim(),
                        state.value.description.trim(),
                        System.currentTimeMillis(),
                        result?.id ?: 0
                    )
                )
            }
            return
        }
    }


    private fun deleteNote() {
        if (result == null) {
            state.value = state.value
            return
        }
        if (state.value.title.isNotBlank() || state.value.description.isNotBlank() && result != null) {
            viewModelScope.launch {
                repository.delete(
                    Note(
                        state.value.title,
                        state.value.description,
                        result!!.lastUpdated,
                        result!!.id
                    )
                )
            }

            return
        }
    }

    fun onTitleChange(title: String) {
        state.value = state.value.copy(title = title)
    }

    fun onTextChange(description: String) {
        state.value = state.value.copy(description = description)
    }

}
