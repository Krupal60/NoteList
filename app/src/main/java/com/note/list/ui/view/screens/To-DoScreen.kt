package com.note.list.ui.view.screens


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.note.list.domain.todo.OnToDoAction
import com.note.list.domain.todo.ToDo
import com.note.list.ui.view.components.ToDoList
import com.note.list.viewmodel.ToDoListViewModel

@Immutable
data class ToDoState(
    val description: String = "",
    val lastUpdated: Long = 0L,
    val isDone: Boolean = false,
    val showDialog: Boolean = false,
    val id: Int = 0
)

@Composable
fun ToDoListScreenMain(viewModel: ToDoListViewModel = hiltViewModel()) {
    val toDoList by
    viewModel.todo.collectAsStateWithLifecycle(initialValue = Result.success(emptyList()))
    val toDoListDone by
    viewModel.todoDone.collectAsStateWithLifecycle(initialValue = Result.success(emptyList()))
    val state by viewModel.state.collectAsStateWithLifecycle()
    ToDoListScreen(toDoList, toDoListDone, state, viewModel::onAction)
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun ToDoListScreen(
    toDoListResult: Result<List<ToDo>>,
    toDoListDoneResult: Result<List<ToDo>>,
    state: ToDoState,
    onAction: (OnToDoAction) -> Unit
) {
    val context = LocalContext.current

    if (state.showDialog) {
        AlertDialog(
            onDismissRequest = {
                onAction(OnToDoAction.HideDialog)
            },
            confirmButton = {
                OutlinedButton(onClick = {
                    if (state.description.length > 280) {
                        Toast.makeText(context, "Only 280 words allow", Toast.LENGTH_SHORT).show()
                    } else {
                        onAction(OnToDoAction.Upsert)
                        onAction(OnToDoAction.HideDialog)
                    }
                }
                ) {
                    Text(text = if (state.id == 0) "Save" else "Update")
                }

            },
            dismissButton = {
                OutlinedButton(onClick = { onAction(OnToDoAction.HideDialog) }) {
                    Text(text = "Cancel")
                }


            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "TO-Do List")

                }
            },
            text = {
                OutlinedTextField(
                    value = state.description,
                    onValueChange = {
                        onAction(OnToDoAction.UpdateDescription(it))
                    },
                    maxLines = 7,
                    isError = state.description.length > 280,
                    placeholder = {
                        Text(text = "Add Description")
                    }
                )
            }

        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "To-Do List",
                    style = MaterialTheme.typography.titleLarge
                )
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    onAction(OnToDoAction.ShowDialog)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Note"
                )
            }
        },
        modifier = Modifier.safeContentPadding()

    ) { paddingValues ->

        LazyColumn(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 100.dp, top = 12.dp)
        ) {
            if (toDoListResult.isSuccess && toDoListDoneResult.isSuccess) {
                val toDoList = toDoListResult.getOrThrow()
                val toDoListDoneItems = toDoListDoneResult.getOrThrow()
                if (toDoList.isEmpty() && toDoListDoneItems.isEmpty()) {
                    stickyHeader {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 14.dp, vertical = 24.dp),
                                textAlign = TextAlign.Center,
                                text = "Nothing Here , Create New To-Do List..",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }
            }

            toDoListResult.onSuccess { toDoList ->

                items(items = toDoList, key = { "todo_${it.id}" }) { todo ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .animateItem(
                                fadeInSpec = tween(durationMillis = 300),
                                fadeOutSpec = tween(durationMillis = 300)
                            )
                            .shadow(
                                elevation = 3.dp,
                                shape = CardDefaults.elevatedShape,
                                clip = true,
                                ambientColor = MaterialTheme.colorScheme.inverseSurface,
                                spotColor = MaterialTheme.colorScheme.inverseSurface
                            )
                    ) {
                        ToDoList(todo, onAction)
                    }
                }

                toDoListDoneResult.onSuccess { toDoListDoneItems ->

                    item {
                        AnimatedVisibility(
                            visible = toDoListDoneItems.isNotEmpty(),
                            enter = fadeIn(animationSpec = tween(durationMillis = 300)) + slideInVertically(
                                animationSpec = tween(durationMillis = 300)
                            ),
                            exit = fadeOut(animationSpec = tween(durationMillis = 300))
                        ) {
                            Text(
                                text = "Completed",
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .padding(vertical = 14.dp)
                            )
                        }
                    }

                    items(toDoListDoneItems, key = { "done_${it.id}" }) { todo ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .animateItem(
                                    fadeInSpec = tween(durationMillis = 300),
                                    fadeOutSpec = tween(durationMillis = 300)
                                )
                                .shadow(
                                    elevation = 3.dp,
                                    shape = CardDefaults.elevatedShape,
                                    clip = true,
                                    ambientColor = MaterialTheme.colorScheme.inverseSurface,
                                    spotColor = MaterialTheme.colorScheme.inverseSurface
                                )
                        ) {
                            ToDoList(todo, onAction)
                        }
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ToDoListScreenPreview() {
    val toDoList by mutableStateOf(
        Result.success(
            listOf(
                ToDo(
                    id = 1,
                    description = "Buy groceries",
                    lastUpdated = System.currentTimeMillis(),
                    isDone = false
                ),
                ToDo(
                    id = 2,
                    description = "Walk the dog",
                    lastUpdated = System.currentTimeMillis(),
                    isDone = false
                )
            )
        )
    )
    val toDoListDone by mutableStateOf(
        Result.success(
            listOf(
                ToDo(
                    id = 3,
                    description = "Pay bills",
                    lastUpdated = System.currentTimeMillis(),
                    isDone = true
                )
            )
        )
    )
    val state by mutableStateOf(ToDoState(showDialog = false))
    val onAction: (OnToDoAction) -> Unit = {}

    ToDoListScreen(
        toDoListResult = toDoList,
        toDoListDoneResult = toDoListDone,
        state = state,
        onAction = onAction
    )
}
