package com.note.list.ui.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.note.list.domain.upsert.OnNoteUpsertAcion
import com.note.list.viewmodel.UpsertViewModel

data class UpsertState(
    val title: String = "",
    val description: String = "",
)

@Composable
fun UpsertMain(
    navController: NavHostController,
    viewModel: UpsertViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

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
    state: State<UpsertState>,
    onAction: (OnNoteUpsertAcion) -> Unit,
    onTitleChange: (String) -> Unit,
    onTextChange: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }, title = {},
                actions = {
                    IconButton(onClick = {
                        onAction(OnNoteUpsertAcion.NoteUpsert)
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Edit or Create"
                        )
                    }

                    IconButton(onClick = {
                        onAction(
                            OnNoteUpsertAcion.Delete
                        )
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = "delete"
                        )
                    }
                }
            )
        }
    ) { padding ->
        val scrollState = rememberScrollState()

        val isOpen by rememberKeyboard()

        LaunchedEffect(key1 = isOpen) {
            if (isOpen) {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding(), start = 12.dp, end = 12.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                value = state.value.title,
                onValueChange = {
                    onTitleChange(it)
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Justify
                ),
                decorationBox = { it ->
                    if (state.value.title.isEmpty()) {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Justify
                            )
                        )
                    }
                    it()
                })



            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(horizontal = 14.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                value = state.value.description,
                onValueChange = {
                    onTextChange(it)
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Justify
                ),
                decorationBox = { it ->
                    if (state.value.description.isEmpty()) {
                        Text(
                            text = "note",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Justify
                            )
                        )
                    }
                    it()
                })

        }
    }
}

@Composable
fun rememberKeyboard(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}
