package com.note.list.domain.note

import androidx.compose.runtime.Immutable
import com.note.list.data.local.note.Notes

@Immutable
data class Note(
    val title: String,
    val description: String,
    val lastUpdated: Long,
    val id: Int = 0 // No need for auto-generation here
)


fun Notes.toNote(): Note {
    return Note(
        title = this.title,
        description = this.description,
        lastUpdated = this.lastUpdated,
        id = this.id
    )
}

fun Note.toNotes():Notes{
    return Notes(
        title = this.title,
        description = this.description,
        lastUpdated = this.lastUpdated,
        id = this.id
    )
}