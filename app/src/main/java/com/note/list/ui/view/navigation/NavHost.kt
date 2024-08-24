package com.note.list.ui.view.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.note.list.ui.view.screens.NoteScreenMain
import com.note.list.ui.view.screens.Screen
import com.note.list.ui.view.screens.ToDoListScreenMain
import com.note.list.ui.view.screens.UpsertMain

@Composable
fun NavHost(
    navController: NavHostController,
    onItemClick :(Int)->Unit,
    onFabClick :(Int)->Unit
) {

    NavHost(navController = navController,
        startDestination = Screen.NoteScreen.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(600)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = 600)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(600)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(600)
            )
        }) {
        composable(Screen.NoteScreen.route) {
            NoteScreenMain( onItemClick = onItemClick::invoke,onFabClick =onFabClick::invoke)
        }
        composable(Screen.ToDoList.route) {
            ToDoListScreenMain()
        }

    }
}