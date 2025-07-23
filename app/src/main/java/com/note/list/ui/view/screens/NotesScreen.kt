package com.note.list.ui.view.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.note.list.domain.note.Note
import com.note.list.viewmodel.NoteViewModel

@Composable
fun NoteScreenMain(
    onItemClick: (Int) -> Unit,
    onFabClick: (Int) -> Unit,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val notes =
        viewModel.notes.collectAsStateWithLifecycle(initialValue = Result.success(emptyList()))
    NoteScreen(notes, onFloatButtonClick = {
        onFabClick(0)
    }, onItemClick = { id ->
        onItemClick(id)
    })
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun NoteScreen(
    notes: State<Result<List<Note>>>, onFloatButtonClick: () -> Unit, onItemClick: (Int) -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Notes", style = MaterialTheme.typography.titleLarge
            )
        })
    }, floatingActionButton = {
        FloatingActionButton(
            shape = CircleShape, onClick = {
                onFloatButtonClick()
            }) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = "Add Note"
            )
        }
    }) { paddingValues ->
        Surface {
            val notesData = notes.value
            notesData.onSuccess { notes ->
                AnimatedContent(notes.isEmpty()) { state ->
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
                                    modifier = Modifier.padding(
                                        horizontal = 14.dp,
                                        vertical = 24.dp
                                    ),
                                    textAlign = TextAlign.Center,
                                    text = "Nothing Here , Create New Note..",
                                    style = MaterialTheme.typography.headlineSmall
                                )

                            }
                        }
                    } else {
                        Column(modifier = Modifier.padding(paddingValues)) {
                            val focusManager = LocalFocusManager.current
                            var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                                mutableStateOf(TextFieldValue("", TextRange(0, 0)))
                            }
                            val searchText = textFieldValue.text.replace(" ", "")
                            val filteredNotes by remember(textFieldValue, notes) {
                                derivedStateOf {
                                    if (searchText.isBlank()) {
                                        notes
                                    } else {
                                        notes.filter { note ->
                                            val title = note.title.replace(" ", "")
                                            val description = note.description.replace(" ", "")
                                            title.contains(
                                                searchText, ignoreCase = true
                                            ) || description.contains(
                                                searchText, ignoreCase = true
                                            ) || title.startsWith(
                                                searchText, ignoreCase = true
                                            ) || description.startsWith(
                                                searchText, ignoreCase = true
                                            )

                                        }
                                    }
                                }
                            }
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
                                        text = "Search Notes",
                                        style = MaterialTheme.typography.bodyMediumEmphasized
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
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
                                isError = filteredNotes.isEmpty() && textFieldValue.text.isNotEmpty(),
                                supportingText = if (filteredNotes.isEmpty() && textFieldValue.text.isNotEmpty()) {
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
                                        }) {
                                            Icon(
                                                Icons.Rounded.Clear,
                                                contentDescription = "Clear Icon"
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
                            Spacer(modifier = Modifier.padding(top = 6.dp))
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    top = 12.dp, bottom = 100.dp, start = 12.dp, end = 12.dp
                                ),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalItemSpacing = 12.dp,
                            ) {
                                items(filteredNotes, key = { it.id }) { note ->
                                    val currentSearchText = textFieldValue.text.replace(" ", "")
                                        .trim() // Use space-removed text
                                    ElevatedCard(
                                        modifier = Modifier.animateItem(),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                                        ),
                                        onClick = {
                                            onItemClick(note.id)
                                        }) {

                                        Column(
                                            modifier = Modifier.padding(
                                                vertical = 10.dp, horizontal = 12.dp
                                            ),
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            val titleVisible = note.title.isNotEmpty()
                                            AnimatedVisibility(visible = titleVisible) {
                                                // THIS IS THE KEY CHANGE for currentSearchText

                                                val originalTitle = note.title.trim()

                                                val titleAnnotatedString = buildAnnotatedString {
                                                    append(originalTitle) // Append the original text with spaces

                                                    if (currentSearchText.isNotEmpty() && originalTitle.isNotEmpty()) {

                                                        val originalDescriptionSpaceless =
                                                            originalTitle.replace(" ", "")
                                                        var searchStartInSpaceless = 0

                                                        while (searchStartInSpaceless < originalDescriptionSpaceless.length) {
                                                            val foundIdxInSpaceless =
                                                                originalDescriptionSpaceless.indexOf(
                                                                    currentSearchText,
                                                                    searchStartInSpaceless,
                                                                    ignoreCase = true
                                                                )

                                                            if (foundIdxInSpaceless == -1) break

                                                            var originalStartIdx = -1
                                                            var originalEndIdx = -1
                                                            var nonSpaceCount = 0
                                                            var originalIteratorIdx = 0

                                                            while (originalIteratorIdx < originalTitle.length) {
                                                                if (originalTitle[originalIteratorIdx] != ' ') {
                                                                    if (nonSpaceCount == foundIdxInSpaceless) {
                                                                        originalStartIdx =
                                                                            originalIteratorIdx
                                                                        break
                                                                    }
                                                                    nonSpaceCount++
                                                                }
                                                                originalIteratorIdx++
                                                            }
                                                            if (foundIdxInSpaceless == 0 && originalStartIdx == -1) {
                                                                originalStartIdx = 0
                                                                while (originalStartIdx < originalTitle.length && originalTitle[originalStartIdx] == ' ') {
                                                                    originalStartIdx++
                                                                }
                                                                if (originalStartIdx >= originalTitle.length && currentSearchText.isNotEmpty()) originalStartIdx =
                                                                    -1 // only spaces
                                                            }

                                                            if (originalStartIdx != -1) {
                                                                nonSpaceCount = 0
                                                                originalIteratorIdx =
                                                                    originalStartIdx
                                                                while (originalIteratorIdx < originalTitle.length && nonSpaceCount < currentSearchText.length) {
                                                                    if (originalTitle[originalIteratorIdx] != ' ') {
                                                                        nonSpaceCount++
                                                                    }
                                                                    originalIteratorIdx++
                                                                }
                                                                if (nonSpaceCount == currentSearchText.length) {
                                                                    originalEndIdx =
                                                                        originalIteratorIdx
                                                                }
                                                            }

                                                            if (originalStartIdx != -1 && originalEndIdx != -1 && originalStartIdx < originalEndIdx) {
                                                                addStyle(
                                                                    style = SpanStyle(background = MaterialTheme.colorScheme.primaryContainer),
                                                                    start = originalStartIdx,
                                                                    end = originalEndIdx
                                                                )
                                                            }

                                                            searchStartInSpaceless =
                                                                foundIdxInSpaceless + currentSearchText.length
                                                            if (searchStartInSpaceless <= foundIdxInSpaceless) {
                                                                searchStartInSpaceless =
                                                                    foundIdxInSpaceless + 1
                                                            }
                                                        }
                                                    }
                                                }
                                                Text(
                                                    text = titleAnnotatedString,
                                                    style = MaterialTheme.typography.titleSmallEmphasized.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                                            }
                                            val descriptionVisible = note.description.isNotEmpty()
                                            AnimatedVisibility(visible = descriptionVisible) {
                                                // THIS IS THE KEY CHANGE for currentSearchText
                                                val originalDescription = note.description.trim()

                                                val descriptionAnnotatedString =
                                                    buildAnnotatedString {
                                                        append(originalDescription) // Append the original text with spaces

                                                        if (currentSearchText.isNotEmpty() && originalDescription.isNotEmpty()) {

                                                            val originalDescriptionSpaceless =
                                                                originalDescription.replace(" ", "")
                                                            var searchStartInSpaceless = 0

                                                            while (searchStartInSpaceless < originalDescriptionSpaceless.length) {
                                                                val foundIdxInSpaceless =
                                                                    originalDescriptionSpaceless.indexOf(
                                                                        currentSearchText,
                                                                        searchStartInSpaceless,
                                                                        ignoreCase = true
                                                                    )

                                                                if (foundIdxInSpaceless == -1) break

                                                                var originalStartIdx = -1
                                                                var originalEndIdx = -1
                                                                var nonSpaceCount = 0
                                                                var originalIteratorIdx = 0

                                                                while (originalIteratorIdx < originalDescription.length) {
                                                                    if (originalDescription[originalIteratorIdx] != ' ') {
                                                                        if (nonSpaceCount == foundIdxInSpaceless) {
                                                                            originalStartIdx =
                                                                                originalIteratorIdx
                                                                            break
                                                                        }
                                                                        nonSpaceCount++
                                                                    }
                                                                    originalIteratorIdx++
                                                                }
                                                                if (foundIdxInSpaceless == 0 && originalStartIdx == -1) {
                                                                    originalStartIdx = 0
                                                                    while (originalStartIdx < originalDescription.length && originalDescription[originalStartIdx] == ' ') {
                                                                        originalStartIdx++
                                                                    }
                                                                    if (originalStartIdx >= originalDescription.length && currentSearchText.isNotEmpty()) originalStartIdx =
                                                                        -1 // only spaces
                                                                }

                                                                if (originalStartIdx != -1) {
                                                                    nonSpaceCount = 0
                                                                    originalIteratorIdx =
                                                                        originalStartIdx
                                                                    while (originalIteratorIdx < originalDescription.length && nonSpaceCount < currentSearchText.length) {
                                                                        if (originalDescription[originalIteratorIdx] != ' ') {
                                                                            nonSpaceCount++
                                                                        }
                                                                        originalIteratorIdx++
                                                                    }
                                                                    if (nonSpaceCount == currentSearchText.length) {
                                                                        originalEndIdx =
                                                                            originalIteratorIdx
                                                                    }
                                                                }

                                                                if (originalStartIdx != -1 && originalEndIdx != -1 && originalStartIdx < originalEndIdx) {
                                                                    addStyle(
                                                                        style = SpanStyle(background = MaterialTheme.colorScheme.primaryContainer),
                                                                        start = originalStartIdx,
                                                                        end = originalEndIdx
                                                                    )
                                                                }

                                                                searchStartInSpaceless =
                                                                    foundIdxInSpaceless + currentSearchText.length
                                                                if (searchStartInSpaceless <= foundIdxInSpaceless) {
                                                                    searchStartInSpaceless =
                                                                        foundIdxInSpaceless + 1
                                                                }
                                                            }
                                                        }
                                                    }
                                                Text(
                                                    text = descriptionAnnotatedString,
                                                    style = MaterialTheme.typography.bodyLargeEmphasized,
                                                    overflow = TextOverflow.Ellipsis,
                                                    maxLines = 10,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }.onFailure {
                // Handle error state, e.g., show an error message
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Failed to load notes: ${it.localizedMessage}")
                }
            }
        }
    }
}