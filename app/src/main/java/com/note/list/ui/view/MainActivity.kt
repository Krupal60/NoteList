package com.note.list.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.note.list.ui.theme.NoteListTheme
import com.note.list.ui.view.navigation.NavHost
import com.note.list.ui.view.screens.Screen
import dagger.hilt.android.AndroidEntryPoint

data class NavItems(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            NoteListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    val navController = rememberNavController()
                    MainContent(navController)
                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3AdaptiveNavigationSuiteApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
)
@Composable
fun MainContent(navController: NavHostController) {

    val navSuiteType =
        calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationSuiteScaffoldLayout(
        layoutType = navSuiteType,
        navigationSuite = {
            if(
              currentBackStackEntry?.destination?.route != Screen.Upsert.route + "/{id}"
            ) {
                NavigationSuite(layoutType = navSuiteType) {
                    val data = listOf(
                        NavItems("Notes", "Notes/{id}", Icons.AutoMirrored.Filled.Notes),
                        NavItems("To-Do List", "To-Do List", Icons.Default.Checklist)
                    )
                    data.forEach { item ->
                        item(
                            selected = currentBackStackEntry?.destination?.let {
                                it.route == item.route
                            } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    if (currentBackStackEntry?.destination?.let { it.route == item.route } == false) {
                                        popUpTo(navController.graph.startDestinationId)
                                    }

                                    launchSingleTop = true
                                    restoreState = true

                                }
                            },

                            label = { Text(text = item.title) },
                            icon = { Icon(item.icon, contentDescription = item.title) }
                        )
                    }
                }

            }
        }
    ) {
        NavHost(navController)
    }
}


//@Composable
//fun NavBar(navController: NavController) {
//    val currentBackStackEntry by navController.currentBackStackEntryAsState()
//    NavigationBar(containerColor = Color.Transparent) {
//        val data = listOf(
//            NavItems("Notes", "Notes/{id}", Icons.AutoMirrored.Filled.Notes),
//            NavItems("To-Do List", "To-Do List", Icons.Default.Checklist)
//        )
//
//        data.forEach { item ->
//            NavigationBarItem(
//                selected = currentBackStackEntry?.destination?.let {
//                    it.route == item.route
//                } == true,
//                onClick = {
//                    navController.navigate(item.route) {
//                        if (currentBackStackEntry?.destination?.let { it.route == item.route } == false) {
//                            popUpTo(navController.graph.startDestinationId)
//                        }
//
//                        launchSingleTop = true
//                        restoreState = true
//
//                    }
//                },
//                label = { Text(text = item.title) },
//                icon = { Icon(item.icon, contentDescription = item.title) })
//        }
//    }
//}