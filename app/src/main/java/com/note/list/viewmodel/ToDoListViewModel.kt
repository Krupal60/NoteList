package com.note.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.list.data.repository.todo.ToDoListRepositoryImpl
import com.note.list.domain.todo.OnToDoAction
import com.note.list.domain.todo.ToDo
import com.note.list.ui.view.screens.ToDoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(private val repository: ToDoListRepositoryImpl) :
    ViewModel() {

    val todo: Flow<Result<List<ToDo>>> = repository.getToDoListByNotDone()
    val todoDone: Flow<Result<List<ToDo>>> = repository.getToDoListByDone()

    var state = MutableStateFlow(ToDoState())
        private set

    fun onAction(onToDoAction: OnToDoAction) {
        when (onToDoAction) {
            OnToDoAction.Upsert -> upsertData()
            is OnToDoAction.UpdateDescription -> updateDescription(onToDoAction.description)
            is OnToDoAction.GetData -> getData(onToDoAction.id)
            is OnToDoAction.Delete -> delete(onToDoAction.toDo)
            is OnToDoAction.IsDone -> isDone(
                onToDoAction.id,
                onToDoAction.isDone,
                onToDoAction.description
            )

            is OnToDoAction.Edit -> edit(onToDoAction.id)
            OnToDoAction.ShowDialog -> showDialog()
            OnToDoAction.HideDialog -> hideDialog()
        }
    }

    private fun hideDialog() {
        state.value = state.value.copy(
            description = "",
            lastUpdated = 0L,
            isDone = false,
            showDialog = false,
            id = 0
        )
    }

    private fun showDialog() {
        state.value = state.value.copy(showDialog = true)
    }

    private fun edit(id: Int) {
        state.value = state.value.copy(id = id)
    }

    private fun isDone(id: Int, isDone: Boolean, description: String) {
        state.value = state.value.copy(isDone = isDone)
        viewModelScope.launch {
            repository.upsert(
                ToDo(
                    description,
                    System.currentTimeMillis(),
                    isDone = isDone,
                    id = id
                )
            )
        }
    }


    private fun delete(todo: ToDo) {
        viewModelScope.launch {
            repository.delete(
                todo
            )
        }
        state.value = state.value.copy(
            description = "",
            lastUpdated = 0L,
            isDone = false,
            showDialog = false,
            id = 0
        )
    }

    private fun getData(id: Int) {
        viewModelScope.launch {
            val it = repository.getToDoListDetail(id)
            state.value =
                state.value.copy(
                    description = it.description,
                    lastUpdated = it.lastUpdated,
                    isDone = it.isDone,
                )
        }
    }

    private fun updateDescription(description: String) {
        state.value = state.value.copy(
            description = description,
            lastUpdated = System.currentTimeMillis()
        )
    }

    private fun upsertData() {
        if (state.value.description.isNotBlank() && state.value.id == 0) {
            viewModelScope.launch {
                repository.upsert(
                    ToDo(
                        state.value.description,
                        System.currentTimeMillis(),
                        isDone = false
                    )
                )
            }
            return
        }
        if (state.value.description.isNotBlank() && state.value.id != 0) {
            viewModelScope.launch {
                repository.upsert(
                    ToDo(
                        state.value.description,
                        System.currentTimeMillis(),
                        isDone = state.value.isDone,
                        id = state.value.id
                    )
                )
            }
            state.value = state.value.copy(
                description = "",
                lastUpdated = 0L,
                isDone = false,
                showDialog = false,
                id = 0
            )
            return
        }

    }

    override fun onCleared() {
        state.value = state.value.copy(
            description = "",
            lastUpdated = 0L,
            isDone = false,
            showDialog = false,
            id = 0
        )
        super.onCleared()
    }


}
