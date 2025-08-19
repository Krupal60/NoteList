package com.note.list.ui.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewManagerFactory
import com.note.list.ui.theme.NoteListTheme
import com.note.list.ui.view.components.HeightSizeClasses
import com.note.list.ui.view.components.WidthSizeClasses
import com.note.list.ui.view.components.minHeight
import com.note.list.ui.view.components.minWidth
import com.note.list.ui.view.navigation.NavHost
import com.note.list.ui.view.navigation.RootScreen
import com.note.list.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@Immutable
data class NavItems(
    val title: String, val route: String, val icon: ImageVector
)


/**
 * Returns whether the lazy list is currently scrolling up.
 */
@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

/**
 * Returns whether the LazyStaggeredGridState list is currently scrolling up.
 */
@Composable
fun LazyStaggeredGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var appUpdateManager: AppUpdateManager
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            // handle callback
            if (result.resultCode != RESULT_OK) {
                makeText(
                    this,
                    "Update flow failed! Result code: ${result.resultCode}",
                    LENGTH_LONG
                ).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        appUpdateManager = AppUpdateManagerFactory.create(this)
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
            val context = LocalContext.current
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activityResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                }
            }
            NoteListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
                ) {
                    val manager = ReviewManagerFactory.create(context)
                    val request = manager.requestReviewFlow()
                    request.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val reviewInfo = task.result
                            manager.launchReviewFlow(this, reviewInfo)
                        }
                    }
                    MainContent()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activityResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                }
            }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    fun navigationSuiteType(adaptiveInfo: WindowAdaptiveInfo): NavigationSuiteType {
        return with(adaptiveInfo) {
            if (windowSizeClass.minWidth == WindowSizeClass.WidthSizeClasses.Compact) {
                NavigationSuiteType.ShortNavigationBarMedium
            } else if (
                windowPosture.isTabletop ||
                windowSizeClass.minHeight == WindowSizeClass.HeightSizeClasses.Compact
            ) {
                NavigationSuiteType.WideNavigationRailCollapsed
            } else {
                NavigationSuiteType.WideNavigationRailCollapsed
            }
        }
    }
    NavigationSuiteScaffold(
        layoutType = navigationSuiteType(windowAdaptiveInfo),
        modifier = Modifier.safeDrawingPadding(), navigationSuiteItems = {
            data.forEach { item ->
                val isSelected = currentRootDestination?.route == item.route
                item(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
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
        Scaffold {
            NavHost(navController)
        }
    }
}
