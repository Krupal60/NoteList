package com.note.list.ui.view.screens


import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.note.list.domain.todo.OnToDoAction
import com.note.list.domain.todo.ToDo
import com.note.list.ui.view.components.ToDoList
import com.note.list.ui.view.isScrollingUp
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
    val toDoList by viewModel.todo.collectAsStateWithLifecycle()
    val toDoListDone by viewModel.todoDone.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    ToDoListScreen(toDoList, toDoListDone, state, viewModel::onAction)
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3ExpressiveApi::class
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
        AlertDialog(onDismissRequest = {
            onAction(OnToDoAction.HideDialog)
        }, confirmButton = {
            Button(
                onClick = {
                    if (state.description.length > 280) {
                        Toast.makeText(context, "Only 280 words allow", Toast.LENGTH_SHORT).show()
                    } else {
                        onAction(OnToDoAction.Upsert)
                        onAction(OnToDoAction.HideDialog)
                    }
                }, shapes = ButtonShapes(
                    shape = ButtonDefaults.shape, pressedShape = RoundedCornerShape(8.dp)
                )

            ) {
                Text(text = if (state.id == 0) "Save" else "Update")
            }

        }, dismissButton = {
            OutlinedButton(
                onClick = { onAction(OnToDoAction.HideDialog) }, shapes = ButtonShapes(
                    shape = ButtonDefaults.shape, pressedShape = RoundedCornerShape(8.dp)
                )
            ) {
                Text(text = "Cancel")
            }


        }, title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "TO-Do List")

            }
        }, text = {
            OutlinedTextField(
                value = state.description,
                onValueChange = {
                    onAction(OnToDoAction.UpdateDescription(it))
                },
                maxLines = 7,
                isError = state.description.length > 280,
                placeholder = {
                    Text(text = "Add Description")
                },
                textStyle = MaterialTheme.typography.bodyMediumEmphasized,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    if (state.description.length > 280) {
                        Toast.makeText(context, "Only 280 words allow", Toast.LENGTH_SHORT).show()
                    } else {
                        onAction(OnToDoAction.Upsert)
                        onAction(OnToDoAction.HideDialog)
                    }
                })
        }

        )
    }
    val lazyListScope = rememberLazyListState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "To-Do List", style = MaterialTheme.typography.titleLarge
            )
        })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            text = {
                Text(
                    text = "Add To-Do", style = MaterialTheme.typography.labelLarge
                )
            },
            expanded = lazyListScope.isScrollingUp(),
            icon = {
                Icon(
                    imageVector = Icons.Filled.Add, contentDescription = "Add To-Do"
                )
            }, onClick = {
                onAction(OnToDoAction.ShowDialog)
            })
    }) { paddingValues ->
        Surface {
            AnimatedContent(
                toDoListResult.getOrThrow().isEmpty() && toDoListDoneResult.getOrThrow().isEmpty()
            ) { state ->
                if (state) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        AnimatedVisibility(true) {
                            Text(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 24.dp),
                                textAlign = TextAlign.Center,
                                text = "Nothing Here , Create New To-Do List..",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                } else {
                    val focusManager = LocalFocusManager.current
                    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                        mutableStateOf(TextFieldValue("", TextRange(0, 0)))
                    }
                    val searchText = textFieldValue.text.replace(" ", "")
                    val filteredTodoList by remember(
                        textFieldValue,
                        toDoListResult,
                        toDoListDoneResult
                    ) {
                        derivedStateOf {
                            if (searchText.isBlank()) {
                                toDoListResult.getOrThrow() + toDoListDoneResult.getOrThrow()
                            } else {
                                val mergeList =
                                    toDoListResult.getOrThrow() + toDoListDoneResult.getOrThrow()
                                mergeList.filter { toDo ->
                                    val description = toDo.description.replace(" ", "")
                                    description.contains(
                                        searchText, ignoreCase = true
                                    ) || description.startsWith(
                                        searchText, ignoreCase = true
                                    )
                                }
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(paddingValues)) {
                        TextField(
                            value = textFieldValue,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            onValueChange = {
                                textFieldValue = textFieldValue.copy(
                                    text = it.text, selection = it.selection
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Rounded.Search, contentDescription = "Search Icon"
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "Search Todos...",
                                    style = MaterialTheme.typography.bodyMediumEmphasized
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                errorContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                cursorColor = MaterialTheme.colorScheme.primary
                            ),
                            isError = filteredTodoList.isEmpty() && textFieldValue.text.isNotEmpty(),
                            supportingText = if (filteredTodoList.isEmpty() && textFieldValue.text.isNotEmpty()) {
                                {
                                    Text(
                                        text = "No Notes found for ${textFieldValue.text}",
                                        style = MaterialTheme.typography.bodyMediumEmphasized,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            } else null,
                            trailingIcon = {
                                AnimatedVisibility(
                                    textFieldValue.text.isNotEmpty()
                                ) {
                                    IconButton(onClick = {
                                        textFieldValue = textFieldValue.copy(
                                            "", TextRange(0, 0)
                                        )
                                        focusManager.clearFocus(true)
                                    }) {
                                        Icon(
                                            Icons.Rounded.Clear, contentDescription = "Clear Icon"
                                        )
                                    }
                                }
                            },
                            textStyle = MaterialTheme.typography.bodyLargeEmphasized,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions {
                                focusManager.clearFocus(true)
                            })

                        Spacer(modifier = Modifier.padding(top = 4.dp))

                        LazyColumn(
                            Modifier
                                .fillMaxSize()
                                .animateContentSize(),
                            state = lazyListScope,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
                        ) {

                            items(
                                items = filteredTodoList.filterNot { it.isDone },
                                key = { "todo_${it.id}" }) { todo ->
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                        .animateItem(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                                    ),
                                ) {
                                    ToDoList(todo, searchText.trim(), onAction)
                                }
                            }

                            if (filteredTodoList.any { it.isDone }) {
                                stickyHeader {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surface),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Completed",
                                            style = MaterialTheme.typography.titleMediumEmphasized,
                                            modifier = Modifier
                                                .animateItem()
                                                .padding(horizontal = 12.dp)
                                                .padding(vertical = 14.dp)
                                        )
                                    }
                                }
                            }

                            items(
                                filteredTodoList.filter { it.isDone },
                                key = { "done_${it.id}" }) { todo ->
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                        .animateItem(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                                    ),
                                ) {
                                    ToDoList(todo, searchText.trim(), onAction)
                                }
                            }
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
    val toDoList by remember {
        mutableStateOf(
            Result.success(
                listOf(
                    ToDo(
                        id = 1,
                        description = "Buy groceries",
                        lastUpdated = System.currentTimeMillis(),
                        isDone = false
                    ), ToDo(
                        id = 2,
                        description = "Walk the dog",
                        lastUpdated = System.currentTimeMillis(),
                        isDone = false
                    )
                )
            )
        )
    }
    val toDoListDone by remember {
        mutableStateOf(
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
    }
    val state by remember { mutableStateOf(ToDoState(showDialog = false)) }
    val onAction: (OnToDoAction) -> Unit = {}

    ToDoListScreen(
        toDoListResult = toDoList,
        toDoListDoneResult = toDoListDone,
        state = state,
        onAction = onAction
    )
}
