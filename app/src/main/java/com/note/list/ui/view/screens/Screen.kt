package com.note.list.ui.view.screens


sealed class RootScreen(val route: String) {
    data object NoteGraph : RootScreen("note_graph")
    data object ToDoList : RootScreen("to_do_list")
}


sealed class NoteScreen(val route: String) {
    data object Notes : NoteScreen("Notes/{id}")
    data object Upsert : NoteScreen("Upsert")
}
