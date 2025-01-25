package com.note.list.ui.view.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.note.list.ui.view.screens.NoteScreenMain
import com.note.list.ui.view.screens.Screen
import com.note.list.ui.view.screens.ToDoListScreenMain

@Composable
fun NavHost(
    navController: NavHostController,
    onItemClick: (Int) -> Unit,
    onFabClick: (Int) -> Unit
) {

    NavHost(navController = navController,
        startDestination = Screen.NoteScreen.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = 500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
            )
        }) {
        composable(Screen.NoteScreen.route) {
            NoteScreenMain(onItemClick = onItemClick::invoke, onFabClick = onFabClick::invoke)
        }
        composable(Screen.ToDoList.route) {
            ToDoListScreenMain()
        }

    }
}