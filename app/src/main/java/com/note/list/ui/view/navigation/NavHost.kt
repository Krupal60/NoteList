package com.note.list.ui.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.note.list.ui.view.screens.NoteScreen
import com.note.list.ui.view.screens.NoteScreenMain
import com.note.list.ui.view.screens.RootScreen
import com.note.list.ui.view.screens.ToDoListScreenMain
import com.note.list.ui.view.screens.UpsertMain

@Composable
fun NavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = RootScreen.NoteGraph.route
    ) {
        noteNavGraph(navController = navController)
        composable(RootScreen.ToDoList.route) {
            ToDoListScreenMain()
        }

    }
}

fun NavGraphBuilder.noteNavGraph(navController: NavHostController) {
    navigation(route = RootScreen.NoteGraph.route, startDestination = NoteScreen.Notes.route) {
        composable(NoteScreen.Notes.route) {
            NoteScreenMain(onItemClick = {
                navController.navigate(NoteScreen.Upsert.route + "/$it") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(NoteScreen.Notes.route)
                }
            }, onFabClick = {
                navController.navigate(NoteScreen.Upsert.route + "/0") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(NoteScreen.Notes.route)
                }
            })
        }
        composable(
            NoteScreen.Upsert.route + "/{id}", arguments = listOf(navArgument("id") {
                defaultValue = 0
                type = NavType.IntType
            })
        ) {
            UpsertMain(navController = navController)
        }

    }
}