package com.note.list.ui.view.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.note.list.ui.view.screens.NoteScreenMain
import com.note.list.ui.view.screens.Screen
import com.note.list.ui.view.screens.ToDoListScreenMain
import com.note.list.ui.view.screens.UpsertMain

@Composable
fun NavHost(navController: NavHostController) {

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
            NoteScreenMain(navController)
        }
        composable(Screen.ToDoList.route) {
            ToDoListScreenMain()
        }
        composable(Screen.Upsert.route + "/{id}", arguments = listOf(
            navArgument("id") {
                defaultValue = 0
                type = NavType.IntType
            }
        )) {

            UpsertMain(navController = navController)
        }

    }
}