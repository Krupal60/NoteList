package com.note.list.domain.todo

sealed class OnToDoAction {
    data object Upsert : OnToDoAction()
    data object ShowDialog : OnToDoAction()
    data object HideDialog : OnToDoAction()
    data class Delete(val toDo: ToDo) : OnToDoAction()
    data class Edit(val id : Int) : OnToDoAction()
    data class IsDone(val id : Int,val isDone:Boolean,val description: String) : OnToDoAction()
    data class GetData(val id : Int) : OnToDoAction()
    data class UpdateDescription(val description: String) : OnToDoAction()
}