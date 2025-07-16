package com.note.list.ui.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
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
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalLayoutDirection
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
            modifier = Modifier.padding(bottom = 8.dp, end = 5.dp),
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
        val layoutDirection = LocalLayoutDirection.current
        val displayCutout = WindowInsets.displayCutout.asPaddingValues()
        val startPadding = displayCutout.calculateStartPadding(layoutDirection)
        val endPadding = displayCutout.calculateEndPadding(layoutDirection)
        notesData.onSuccess { notes ->
            if (notes.isEmpty()) {
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
                    AnimatedVisibility(notes.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 24.dp),
                            textAlign = TextAlign.Center,
                            text = "Nothing Here , Create New Note..",
                            style = MaterialTheme.typography.headlineSmall
                        )

                    }
                }
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding().plus(6.dp),
                    bottom = paddingValues.calculateBottomPadding().plus(6.dp),
                    start = startPadding.coerceAtLeast(14.dp),
                    end = endPadding.coerceAtLeast(14.dp)
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalItemSpacing = 12.dp,
            ) {

                items(notes, key = { it.id }) { note ->
                    ElevatedCard(
                        modifier = Modifier.animateItem(),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.elevatedCardElevation(
                            4.dp,
                            4.dp,
                            4.dp,
                            4.dp,
                            4.dp,
                            4.dp
                        ),
                        onClick = {
                            onItemClick(note.id)
                        }) {

                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
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