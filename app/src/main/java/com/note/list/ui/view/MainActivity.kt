package com.note.list.ui.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.note.list.ui.theme.NoteListTheme
import com.note.list.ui.view.navigation.NavHost
import com.note.list.ui.view.screens.RootScreen
import com.note.list.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@Immutable
data class NavItems(
    val title: String, val route: String, val icon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading
        }
        enableEdgeToEdge(
            SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            NoteListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
                ) {
                    MainContent()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainContent() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val data = remember {
        listOf(
            NavItems("Notes", RootScreen.NoteGraph.route, Icons.AutoMirrored.Outlined.Notes),
            NavItems("To-Do List", RootScreen.ToDoList.route, Icons.Default.Checklist)
        )
    }
    val colorScheme = MaterialTheme.colorScheme

    val wideNavigationRailDefaults =
        WideNavigationRailDefaults.colors(
            containerColor = colorScheme.surface,
            modalContainerColor = colorScheme.surface,
        )
    val currentRootDestination = currentBackStackEntry?.destination
        ?.hierarchy
        ?.firstOrNull { navDestination ->
            data.any { it.route == navDestination.route }
        }
    NavigationSuiteScaffold(
        modifier = Modifier.safeDrawingPadding(), navigationSuiteItems = {
            data.forEach { item ->
                val isSelected = currentRootDestination?.route == item.route
                item(
                    selected = isSelected,
                    onClick = {
                        if (currentBackStackEntry?.destination?.let { it.route == item.route } == false) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    label = { Text(text = item.title) },
                    icon = { Icon(item.icon, contentDescription = item.title) }
                )
            }
        }, navigationSuiteColors = NavigationSuiteDefaults.colors(
            shortNavigationBarContainerColor = MaterialTheme.colorScheme.surface,
            wideNavigationRailColors = wideNavigationRailDefaults,
            navigationBarContainerColor = MaterialTheme.colorScheme.surface,
            navigationRailContainerColor = MaterialTheme.colorScheme.surface,
            navigationDrawerContainerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        NavHost(navController)
    }
}
