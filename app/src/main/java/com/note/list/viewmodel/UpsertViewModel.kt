package com.note.list.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.list.data.repository.note.NoteRepositoryImpl
import com.note.list.domain.note.Note
import com.note.list.domain.upsert.OnNoteUpsertAcion
import com.note.list.ui.view.screens.UpsertState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertViewModel @Inject constructor(
    private val repository: NoteRepositoryImpl,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id = savedStateHandle.get<Int>("id")

    var state = MutableStateFlow(UpsertState())
        private set

    private var result: Note? = null

    init {
        getData()
    }

    private fun getData(){
        viewModelScope.launch {
            if (id != null) {
                repository.getNotesDetail(id).collect { resultData ->
                    resultData.map {
                        result = it
                        Log.i("result", result.toString())
                        state.value = state.value.copy(
                            title = result!!.title,
                            description = result!!.description
                        )
                    }
                }

            }

        }
    }


    fun onAction(onNoteUpsertAcion: OnNoteUpsertAcion) {
        when (onNoteUpsertAcion) {
            is OnNoteUpsertAcion.Delete -> deleteNote()
            is OnNoteUpsertAcion.NoteUpsert -> upsertNote()
        }
    }

    private fun upsertNote() {
        if (state.value.title.isNotBlank() || state.value.description.isNotBlank() ) {
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
//        viewModelScope.launch {
//            if (state.value.title.isNotEmpty() || state.value.description.isNotEmpty()) {
//                repository.upsert(
//                    Note(
//                        state.value.title,
//                        state.value.description,
//                        System.currentTimeMillis()
//                    )
//                )
//            }
//        }
    }

    fun onTextChange(description: String) {
        state.value = state.value.copy(description = description)
//        viewModelScope.launch {
//            if (state.value.title.isNotEmpty() || state.value.description.isNotEmpty()) {
//                repository.upsert(
//                    Note(
//                        state.value.title,
//                        state.value.description,
//                        System.currentTimeMillis()
//                    )
//                )
//            }
//        }

    }

}
