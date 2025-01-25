package com.note.list.domain.todo

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable


@Stable
sealed class OnToDoAction {
    @Stable
    data object Upsert : OnToDoAction()

    @Stable
    data object ShowDialog : OnToDoAction()

    @Immutable
    data object HideDialog : OnToDoAction()

    @Immutable
    data class Delete(val toDo: ToDo) : OnToDoAction()

    @Immutable
    data class Edit(val id: Int) : OnToDoAction()

    @Immutable
    data class IsDone(
        val id: Int,
        val isDone: Boolean,
        val description: String
    ) : OnToDoAction()

    @Immutable
    data class GetData(val id: Int) : OnToDoAction()

    @Immutable
    data class UpdateDescription(val description: String) : OnToDoAction()
}