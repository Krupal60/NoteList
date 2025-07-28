package com.note.list.ui.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.note.list.domain.todo.OnToDoAction
import com.note.list.domain.todo.ToDo

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ToDoList(
    todo: ToDo,
    currentSearchText: String,
    onAction: (OnToDoAction) -> Unit
) {

    val originalTitle = todo.description.trim()

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
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Checkbox(modifier = Modifier.weight(0.2f),
            checked = todo.isDone,
            onCheckedChange = {
                onAction(
                    OnToDoAction.IsDone(
                        id = todo.id,
                        isDone = it,
                        description = todo.description
                    )
                )
            }
        )
        Text(
            text = titleAnnotatedString,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodyMediumEmphasized,
            color = if (todo.isDone) Color.Gray else Color.Unspecified,
            textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        AnimatedVisibility(!todo.isDone) {
            IconButton(modifier = Modifier.weight(0.2f),
                onClick = {
                    onAction(OnToDoAction.GetData(todo.id))
                    onAction(OnToDoAction.Edit(id = todo.id))
                    onAction(OnToDoAction.ShowDialog)
                }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit"
                )
            }
        }
        AnimatedVisibility(todo.isDone) {
            Spacer(modifier = Modifier.weight(0.2f))
        }
        IconButton(modifier = Modifier.weight(0.2f),
            onClick = {
                onAction.invoke(OnToDoAction.Delete(todo))
            }
        ) {
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = "Delete"
            )
        }
    }


}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun ToDoListPreview() {
    val todo =
        ToDo(description = "Sample ToDo", lastUpdated = System.currentTimeMillis(), isDone = false)
    ToDoList(todo = todo, "", onAction = {})
}



