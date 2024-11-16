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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.note.list.domain.todo.OnToDoAction
import com.note.list.domain.todo.ToDo

@Composable
fun ToDoList(
    todo: ToDo,
    onAction: (OnToDoAction) -> Unit
) {

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
            text = todo.description,
            textAlign = TextAlign.Justify,
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
                onAction(OnToDoAction.Delete(todo))
            }
        ) {
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = "Delete"
            )
        }
    }


}

