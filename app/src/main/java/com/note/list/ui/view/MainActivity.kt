package com.note.list.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.note.list.ui.theme.NoteListTheme
import com.note.list.ui.view.navigation.NavHost
import com.note.list.ui.view.screens.Screen
import com.note.list.ui.view.screens.UpsertMain
import com.note.list.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class NavItems(
    val title: String, val route: String, val icon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading
        }
        super.onCreate(savedInstanceState)
        setContent {
            NoteListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
                ) {
                    val navController = rememberNavController()
                    androidx.navigation.compose.NavHost(navController = navController,
                        startDestination = Screen.MainScreen.route,
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(600)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(600)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(600)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(600)
                            )
                        }) {


                        composable(
                            Screen.Upsert.route + "/{id}", arguments = listOf(navArgument("id") {
                                defaultValue = 0
                                type = NavType.IntType
                            })
                        ) {
                            UpsertMain(navController = navController)
                        }




                        composable(Screen.MainScreen.route) {
                            MainContent(onFabClick = {
                                navController.navigate(Screen.Upsert.route + "/0") {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(Screen.NoteScreen.route)
                                }
                            }, onItemClick = {
                                navController.navigate(Screen.Upsert.route + "/$it") {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(Screen.NoteScreen.route)
                                }
                            })
                        }

                    }

                }
            }
        }
    }
}


@Composable
fun MainContent(onItemClick: (Int) -> Unit, onFabClick: (Int) -> Unit) {
    val navController = rememberNavController()
    val navSuiteType = calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val data = listOf(
        NavItems("Notes", "Notes/{id}", Icons.AutoMirrored.Filled.Notes),
        NavItems("To-Do List", "To-Do List", Icons.Default.Checklist)
    )


    NavigationSuiteScaffold(layoutType = navSuiteType, navigationSuiteItems = {

        data.forEach { item ->
            item(selected = currentBackStackEntry?.destination?.let {
                it.route == item.route
            } == true,
                onClick = {
                    if (currentBackStackEntry?.destination?.let { it.route == item.route } == false) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },

                label = { Text(text = item.title) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                interactionSource = NoRippleInteractionSource()

            )
        }

    }) {
        NavHost(navController, onItemClick = { onItemClick(it) }, onFabClick = { onFabClick(it) })
    }
}


class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true

}