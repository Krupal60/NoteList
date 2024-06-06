package com.note.list.domain.upsert


sealed class OnNoteUpsertAcion {
    data object Delete : OnNoteUpsertAcion()
    data object NoteUpsert : OnNoteUpsertAcion()
}