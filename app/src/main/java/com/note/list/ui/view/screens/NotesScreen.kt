package com.note.list.ui.view.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteScreen(
    notes: State<Result<List<Note>>>, onFloatButtonClick: () -> Unit, onItemClick: (Int) -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Notes",
                style = MaterialTheme.typography.titleLarge
            )
        })
    }, floatingActionButton = {
        FloatingActionButton(
            shape = CircleShape,
            onClick = {
                onFloatButtonClick()
            }) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = "Add Note"
            )
        }
    }
    ) { paddingValues ->
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
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 24.dp),
                                textAlign = TextAlign.Center,
                                text = "Nothing Here , Create New Note..",
                                style = MaterialTheme.typography.headlineSmall
                            )

                        }
                    }
                } else {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(
                            top = 12.dp, bottom = 100.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalItemSpacing = 12.dp,
                    ) {
                        items(notes, key = { it.id }) { note ->
                            ElevatedCard(
                                modifier = Modifier
                                    .animateItem()
                                    .padding(horizontal = 12.dp),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    onItemClick(note.id)
                                }) {

                                Column(
                                    modifier = Modifier.padding(
                                        vertical = 10.dp,
                                        horizontal = 12.dp
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    AnimatedVisibility(visible = note.title.isNotEmpty()) {
                                        Text(
                                            text = note.title,
                                            textAlign = TextAlign.Justify,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                textAlign = TextAlign.Justify,
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.fillMaxWidth()

                                        )
                                        Spacer(modifier = Modifier.padding(vertical = 8.dp))
                                    }
                                    AnimatedVisibility(visible = note.description.isNotEmpty()) {
                                        Text(
                                            text = note.description,
                                            textAlign = TextAlign.Justify,
                                            style = MaterialTheme.typography.bodyLarge
                                                .copy(textAlign = TextAlign.Justify),
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
    }

}