package com.note.list.ui.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.note.list.domain.upsert.OnNoteUpsertAction
import com.note.list.viewmodel.UpsertViewModel
import kotlinx.coroutines.delay

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Upsert(
    navController: NavHostController,
    state: UpsertState,
    onAction: (OnNoteUpsertAction) -> Unit,
    onTitleChange: (String) -> Unit,
    onTextChange: (String) -> Unit
) {
    DisposableEffect(Unit) {
        onDispose {
            onAction(OnNoteUpsertAction.NoteUpsert)
        }
    }
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
        Surface {
            val focusManager = LocalFocusManager.current
            val descriptionFocusRequester = remember { FocusRequester() }

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
                        .shadow(1.dp, RoundedCornerShape(10.dp)),
                    textStyle = MaterialTheme.typography.titleMediumEmphasized.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify
                    ),
                    maxLines = 3,
                    placeholder = {
                        Text(
                            text = "Enter Title Here...",
                            style = MaterialTheme.typography.titleMediumEmphasized.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.7f
                                ),
                                textAlign = TextAlign.Justify
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
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
                        .focusRequester(descriptionFocusRequester)
                        .imePadding()
                        .padding(
                            horizontal = 12.dp
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .padding(
                            top = 12.dp
                        )
                        .shadow(1.dp, RoundedCornerShape(10.dp)),
                    textStyle = MaterialTheme.typography.bodyLargeEmphasized.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus(true)
                    },
                    placeholder = {
                        Text(
                            text = "Enter your note here...",
                            style = MaterialTheme.typography.bodyLargeEmphasized.copy(
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
                val currentDescriptionIsEmpty by rememberUpdatedState(newValue = state.description.isEmpty())

                LaunchedEffect(Unit) {
                    delay(200)
                    if (currentDescriptionIsEmpty) {
                        descriptionFocusRequester.requestFocus()
                    }
                }
            }
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
