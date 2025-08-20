package com.note.list.ui.view.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.note.list.ui.view.screens.NoteScreenMain
import com.note.list.ui.view.screens.ToDoListScreenMain
import com.note.list.ui.view.screens.UpsertMain

@Composable
fun NavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = RootScreen.NoteGraph.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = spring()
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = spring()
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = spring()
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = spring()
            )
        }
    ) {
        noteNavGraph(navController = navController)
        composable(RootScreen.ToDoList.route) {
            ToDoListScreenMain()
        }

    }
}

fun NavGraphBuilder.noteNavGraph(navController: NavHostController) {
    navigation(route = RootScreen.NoteGraph.route, startDestination = NoteScreen.Notes.route) {
        composable(
            NoteScreen.Notes.route
        ) {
            NoteScreenMain(onItemClick = {
                navController.navigate(NoteScreen.Upsert.route + "/$it") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(NoteScreen.Notes.route)
                }
            }, onFabClick = {
                navController.navigate(NoteScreen.Upsert.route + "/-1") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(NoteScreen.Notes.route)
                }
            })
        }
        composable(
            NoteScreen.Upsert.route + "/{id}", arguments = listOf(navArgument("id") {
                defaultValue = -1
                type = NavType.IntType
            })
        ) {
            UpsertMain(navController = navController)
        }

    }
}