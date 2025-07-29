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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.note.list.domain.todo.OnToDoAction
import com.note.list.domain.todo.ToDo
import com.note.list.ui.view.components.DpHeightSizeClasses.Compact
import com.note.list.ui.view.components.DpHeightSizeClasses.Expanded
import com.note.list.ui.view.components.DpHeightSizeClasses.Medium
import com.note.list.ui.view.components.DpWidthSizeClasses.Compact
import com.note.list.ui.view.components.DpWidthSizeClasses.Expanded
import com.note.list.ui.view.components.DpWidthSizeClasses.ExtraLarge
import com.note.list.ui.view.components.DpWidthSizeClasses.Large
import com.note.list.ui.view.components.DpWidthSizeClasses.Medium

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
        Checkbox(
            modifier = Modifier.weight(0.2f),
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
            IconButton(
                modifier = Modifier.weight(0.2f),
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
        IconButton(
            modifier = Modifier.weight(0.2f),
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

/**
 * The set of width size classes in DP. These values are the lower bounds for the corresponding size
 * classes.
 *
 * @see DpWidthSizeClasses
 */
internal val WindowSizeClass.Companion.WidthSizeClasses
    get() = DpWidthSizeClasses

/**
 * The set of height size classes in DP. These values are the lower bounds for the corresponding
 * size classes.
 *
 * @see DpHeightSizeClasses
 */
internal val WindowSizeClass.Companion.HeightSizeClasses
    get() = DpHeightSizeClasses

/**
 * The set of width size classes in DP. These values are the lower bounds for the corresponding size
 * classes.
 *
 * This object defines different width size classes, including:
 * - [Compact]: Represents the smallest width size class, starting at 0 dp.
 * - [Medium]: Represents a medium width size class, starting at 600 dp.
 * - [Expanded]: Represents an expanded width size class, starting at 840 dp.
 * - [Large]: Represents a large width size class, starting at 1200 dp.
 * - [ExtraLarge]: Represents an extremely large width size class, starting at 1600 dp.
 *
 * These values are used to define breakpoints for adaptive layouts, and are intended to align with
 * the window size class definitions.
 *
 * @see WindowSizeClass
 */
@Suppress("PrimitiveInCollection")
internal object DpWidthSizeClasses {
    /**
     * The lower bound for the Compact width size class. By default, any window width which is at
     * least this value and less than [Medium] will be considered [Compact].
     */
    val Compact = 0.dp

    /**
     * The lower bound for the Medium width size class. By default, any window width which is at
     * least this value and less than [Expanded] will be considered [Medium].
     *
     * @see WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
     */
    val Medium = WIDTH_DP_MEDIUM_LOWER_BOUND.dp

    /**
     * The lower bound for the Expanded width size class. By default, any window width which is at
     * least this value will be considered [Expanded]; or in the V2 definition of the default width
     * size classes, any window width which is at least this value and less than [Large] will be
     * considered [Expanded].
     *
     * @see WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
     */
    val Expanded = WIDTH_DP_EXPANDED_LOWER_BOUND.dp

    /**
     * The lower bound for the Large width size class. With the V2 definition of the default width
     * size, any window width which is at least this value and less than [ExtraLarge] will be
     * considered [Large].
     */
    // TODO(conradchen): Move to window-core definition when it goes to 1.5 stable
    val Large = 1200.dp

    /**
     * The lower bound for the Extra-Large width size class. With the V2 definition of the default
     * width size, any window width which is at least this value will be considered [ExtraLarge].
     */
    // TODO(conradchen): Move to window-core definition when it goes to 1.5 stable
    val ExtraLarge = 1600.dp
}

/**
 * The set of height size classes in DP. These values are the lower bounds for the corresponding
 * size classes.
 *
 * This object defines different height size classes, including:
 * - [Compact]: Represents the smallest height size class, starting at 0 dp.
 * - [Medium]: Represents a medium height size class, starting at 480 dp.
 * - [Expanded]: Represents an expanded height size class, starting at 900 dp.
 *
 * These values are used to define breakpoints for adaptive layouts, and are intended to align with
 * the window size class definitions.
 *
 * @see WindowSizeClass
 */
@Suppress("PrimitiveInCollection")
internal object DpHeightSizeClasses {
    /**
     * The lower bound for the Compact height size class. By default, any window height which is at
     * least this value and less than [Medium] will be considered [Compact].
     */
    val Compact = 0.dp

    /**
     * The lower bound for the Medium height size class. By default, any window height which is at
     * least this value and less than [Expanded] will be considered [Medium].
     *
     * @see WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
     */
    val Medium = HEIGHT_DP_MEDIUM_LOWER_BOUND.dp

    /**
     * The lower bound for the Expanded height size class. By default, any window height which is at
     * least this value will be considered [Expanded].
     *
     * @see WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND
     */
    val Expanded = HEIGHT_DP_EXPANDED_LOWER_BOUND.dp
}

/**
 * The lower bound width of the window size class in [Dp]. This is used to determine which size
 * class a given window size belongs to.
 */
internal val WindowSizeClass.minWidth
    get() = minWidthDp.dp

/**
 * The lower bound height of the window size class in [Dp]. This is used to determine which size
 * class a given window size belongs to.
 */
internal val WindowSizeClass.minHeight
    get() = minHeightDp.dp


