package com.note.list.domain.upsert

import androidx.compose.runtime.Stable

@Stable
sealed class OnNoteUpsertAction {
    @Stable
    data object Delete : OnNoteUpsertAction()

    @Stable
    data object NoteUpsert : OnNoteUpsertAction()
}