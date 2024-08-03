package com.note.list.ui.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
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
    Scaffold(modifier = Modifier
        .navigationBarsPadding()
        .fillMaxSize()
       , topBar = {
        TopAppBar(title = {
            Text(
                text = "Notes",
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 10.dp),
                style = MaterialTheme.typography.titleLarge
            )
        })
    }, floatingActionButton = {
        FloatingActionButton(modifier = Modifier.padding(bottom = 12.dp, end = 5.dp),
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                        start = 14.dp,
                        end = 14.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                if (notes.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 24.dp),
                        textAlign = TextAlign.Center,
                        text = "Nothing Here , Create New Note..",
                        style = MaterialTheme.typography.headlineSmall
                    )

                }
            }
        }
        notesData.onSuccess { notes ->
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                        start = 14.dp,
                        end = 14.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalItemSpacing = 10.dp,
            ) {

                items(notes) { note ->
                    ElevatedCard(modifier = Modifier.animateItemPlacement(
                        animationSpec = tween(
                            durationMillis = 300
                        )
                    ), shape = RoundedCornerShape(10.dp), onClick = {
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
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Serif,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.fillMaxWidth()

                                )
                                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                            }
                            AnimatedVisibility(visible = note.description.isNotEmpty()) {
                                Text(
                                    text = note.description,
                                    textAlign = TextAlign.Justify,
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily.Serif,
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