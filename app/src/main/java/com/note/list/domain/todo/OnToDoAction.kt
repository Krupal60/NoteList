package com.note.list.domain.todo

import androidx.compose.runtime.Stable

//@Stable
//sealed class OnToDoAction {
//    data object Upsert : OnToDoAction()
//    data object ShowDialog : OnToDoAction()
//    data object HideDialog : OnToDoAction()
//    data class Delete(val toDo: ToDo) : OnToDoAction()
//    data class Edit(val id: Int) : OnToDoAction()
//    data class IsDone(val id: Int, val isDone: Boolean, val description: String) : OnToDoAction()
//    data class GetData(val id: Int) : OnToDoAction()
//    data class UpdateDescription(val description: String) : OnToDoAction()
//}

@Stable
sealed class OnToDoAction {
    @Stable
    data object Upsert : OnToDoAction()

    @Stable
    data object ShowDialog : OnToDoAction()

    @Stable
    data object HideDialog : OnToDoAction()

    @Stable
    data class Delete(val toDo: ToDo) : OnToDoAction()

    @Stable
    data class Edit(val id: Int) : OnToDoAction()

    @Stable
    data class IsDone(
        val id: Int,
        val isDone: Boolean,
        val description: String
    ) : OnToDoAction()

    @Stable
    data class GetData(val id: Int) : OnToDoAction()

    @Stable
    data class UpdateDescription(val description: String) : OnToDoAction()
}