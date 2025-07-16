package com.note.list.ui.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.tappableElement
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.note.list.domain.upsert.OnNoteUpsertAction
import com.note.list.viewmodel.UpsertViewModel

@Immutable
data class UpsertState(
    val title: String = "",
    val description: String = "",
)

@Composable
fun UpsertMain(
    navController: NavHostController,
    viewModel: UpsertViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Upsert(
        navController, state,
        viewModel::onAction,
        viewModel::onTitleChange,
        viewModel::onTextChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Upsert(
    navController: NavHostController,
    state: UpsertState,
    onAction: (OnNoteUpsertAction) -> Unit,
    onTitleChange: (String) -> Unit,
    onTextChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }, title = {},
                actions = {
                    IconButton(onClick = {
                        onAction(OnNoteUpsertAction.NoteUpsert)
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Edit or Create"
                        )
                    }
                    if (state.title.isNotEmpty() || state.description.isNotEmpty()) {
                        IconButton(onClick = {
                            onAction(
                                OnNoteUpsertAction.Delete
                            )
                            navController.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.DeleteForever,
                                contentDescription = "delete"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        val layoutDirection = LocalLayoutDirection.current
        val displayCutout = WindowInsets.displayCutout.asPaddingValues()
        val startPadding = displayCutout.calculateStartPadding(layoutDirection)
        val endPadding = displayCutout.calculateEndPadding(layoutDirection)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                value = state.title,
                onValueChange = {
                    onTitleChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp
                    )
                    .wrapContentHeight()
                    .padding(
                        start = startPadding.coerceAtLeast(6.dp),
                        end = endPadding.coerceAtLeast(6.dp)
                    ),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Justify
                ),
                maxLines = 3,
                placeholder = {
                    Text(
                        text = "Enter Title Here...",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.7f
                            ),
                            textAlign = TextAlign.Justify
                        )
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )


            TextField(
                value = state.description,
                onValueChange = {
                    onTextChange(it)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(
                        horizontal = 12.dp
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .padding(
                        top = 12.dp
                    )
                    .padding(
                        start = startPadding.coerceAtLeast(6.dp),
                        end = endPadding.coerceAtLeast(6.dp)
                    ),

                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Justify
                ),
                placeholder = {
                    Text(
                        text = "Enter your note here...",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.7f
                            ),
                            textAlign = TextAlign.Justify
                        )
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview
@Composable
fun UpsertPreview() {
    val navController = rememberNavController()
    val state =
        UpsertState(title = "Sample Title", description = "Sample description text for the note.")
    Upsert(
        navController = navController,
        state = state,
        onAction = {},
        onTitleChange = {},
        onTextChange = {}
    )
}



@Composable
fun ProtectNavigationBar(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val tappableElement = WindowInsets.tappableElement
    val bottomPixels = tappableElement.getBottom(density)
    val usingTappableBars = remember(bottomPixels) {
        bottomPixels != 0
    }
    val barHeight = remember(bottomPixels) {
        tappableElement.asPaddingValues(density).calculateBottomPadding()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        if (usingTappableBars) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .height(barHeight)
            )
        }
    }
}

@Composable
fun rememberKeyboard(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}
