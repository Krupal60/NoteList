package com.note.list.ui.view.screens

sealed class Screen(val route : String) {
    data object MainScreen : Screen("main")
    data object NoteScreen : Screen("Notes/{id}")
    data object ToDoList : Screen("To-Do List")
    data object Upsert : Screen("Upsert")
}