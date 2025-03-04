package com.note.list.ui.view.screens


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.coerceAtLeast
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
    val toDoList =
        viewModel.todo.collectAsStateWithLifecycle(initialValue = Result.success(emptyList()))
    val toDoListDone =
        viewModel.todoDone.collectAsStateWithLifecycle(initialValue = Result.success(emptyList()))
    val state = viewModel.state.collectAsStateWithLifecycle()
    ToDoListScreen(toDoList, toDoListDone, state, viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ToDoListScreen(
    toDoList: State<Result<List<ToDo>>>,
    toDoListDone: State<Result<List<ToDo>>>,
    state: State<ToDoState>,
    onAction: (OnToDoAction) -> Unit
) {
    val context = LocalContext.current

    if (state.value.showDialog) {
        AlertDialog(
            onDismissRequest = {
                onAction(OnToDoAction.HideDialog)
            },
            confirmButton = {
                OutlinedButton(onClick = {
                    if (state.value.description.length > 280) {
                        Toast.makeText(context, "Only 280 words allow", Toast.LENGTH_SHORT).show()
                    } else {
                        onAction(OnToDoAction.Upsert)
                        onAction(OnToDoAction.HideDialog)
                    }
                }
                ) {
                    Text(text = if (state.value.id == 0) "Save" else "Update")
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
                    value = state.value.description,
                    onValueChange = {
                        onAction(OnToDoAction.UpdateDescription(it))
                    },
                    maxLines = 7,
                    isError = state.value.description.length > 280,
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
                modifier = Modifier.padding(bottom = 8.dp, end = 5.dp),
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
        }

    ) { paddingValues ->
        val toDoListData = toDoList.value
        val toDoListDone = toDoListDone.value

        val layoutDirection = LocalLayoutDirection.current
        val displayCutout = WindowInsets.displayCutout.asPaddingValues()
        val startPadding = displayCutout.calculateStartPadding(layoutDirection)
        val endPadding = displayCutout.calculateEndPadding(layoutDirection)
        toDoListData.onSuccess { toDoList ->
            if (toDoList.isEmpty() && toDoListDone.getOrNull().isNullOrEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding(),
                            start = startPadding.coerceAtLeast(14.dp),
                            end = endPadding.coerceAtLeast(14.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {

                    AnimatedVisibility(
                        toDoList.isEmpty() && toDoListDone.getOrNull().isNullOrEmpty()
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

        LazyColumn(
            Modifier
                .fillMaxSize()
                .animateContentSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding().plus(5.dp),
                bottom = paddingValues.calculateBottomPadding().plus(5.dp),
                start = startPadding.coerceAtLeast(14.dp),
                end = endPadding.coerceAtLeast(14.dp)
            )
        ) {
            toDoListData.onSuccess { toDoList ->
                items(toDoList, key = { "todo_${it.id}" }) { todo ->
                    ElevatedCard(
                        modifier = Modifier.animateItem(),
                        elevation = CardDefaults.elevatedCardElevation(
                            4.dp,
                            4.dp,
                            4.dp,
                            4.dp,
                            4.dp,
                            4.dp
                        ),
                        onClick = {
                            onAction(OnToDoAction.GetData(todo.id))
                            onAction(OnToDoAction.Edit(todo.id))
                            onAction(OnToDoAction.ShowDialog)
                        }) {
                        ToDoList(todo, onAction)
                    }
                }
            }

            toDoListDone.onSuccess { toDoListDone ->
                item {
                    AnimatedVisibility(visible = toDoListDone.isNotEmpty()) {
                        Text(text = "Completed", modifier = Modifier.padding(vertical = 10.dp))
                    }
                }
                items(toDoListDone, key = { "done_${it.id}" }) { todo ->
                    ElevatedCard(
                        elevation = CardDefaults.elevatedCardElevation(
                            4.dp,
                            4.dp,
                            4.dp,
                            4.dp,
                            4.dp,
                            4.dp
                        ),
                        modifier = Modifier.animateItem()
                    ) {
                        ToDoList(todo, onAction)
                    }
                }
            }
        }

    }
}