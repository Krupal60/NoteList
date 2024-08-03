package com.note.list.ui.view.screens


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.note.list.domain.todo.OnToDoAction
import com.note.list.domain.todo.ToDo
import com.note.list.ui.view.components.ToDoList
import com.note.list.viewmodel.ToDoListViewModel

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

    AnimatedVisibility(state.value.showDialog) {
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
                    Text(text = "Save")
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
//                    IconButton(
//                        onClick = {
//
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Timer,
//                            contentDescription = "Timer"
//                        )
//                    }
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
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 10.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 12.dp, end = 5.dp),
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


        toDoListData.onSuccess { toDoList ->
            Column (  modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 14.dp, end = 14.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround){
                if (toDoList.isEmpty()) {

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


        LazyColumn(
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .animateContentSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 14.dp,
                    end = 14.dp
                ), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            toDoListData.onSuccess { toDoList ->

                items(toDoList) { todo ->
                    ElevatedCard(
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        ),
                        onClick = {
                            onAction(OnToDoAction.GetData(todo.id))
                            onAction(OnToDoAction.ShowDialog)
                        }) {
                        ToDoList(todo, onAction)
                    }
                }
            }

            val toDoListDoneData = toDoListDone.value
            toDoListDoneData.onSuccess { toDoListDone ->
                item {
                    AnimatedVisibility(visible = toDoListDone.isNotEmpty()) {
                        Text(text = "Completed", modifier = Modifier.padding(vertical = 10.dp))
                    }
                }
                items(toDoListDone) { todo ->

                    ElevatedCard(
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        )
                    ) {
                        ToDoList(todo, onAction)
                    }
                }
            }
        }

    }
}