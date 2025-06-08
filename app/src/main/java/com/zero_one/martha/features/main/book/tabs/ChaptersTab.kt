package com.zero_one.martha.features.main.book.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zero_one.martha.R
import com.zero_one.martha.data.domain.model.Chapter
import com.zero_one.martha.features.main.book.ui.ChaptersListRow
import kotlinx.coroutines.launch

@Composable
fun ChaptersTab(
    chapters: List<Chapter>,
    sortType: Boolean,
    changeSortType: () -> Unit,
    onNavigateToReader: (bookId: UInt, chapterId: UInt) -> Unit,
    onNavigateToPlayer: (bookId: UInt, chapterId: UInt) -> Unit,
    onNavigateToLoginPage: () -> Unit,
    isAuth: () -> Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val listState = rememberLazyListState()
        val sortedChapters = remember(sortType) {
            derivedStateOf {
                if (sortType) {
                    chapters.sortedByDescending {it.serial}
                } else {
                    chapters.sortedBy {it.serial}
                }
            }
        }
        val firstCommentVisibility by remember {
            derivedStateOf {listState.firstVisibleItemIndex}
        }
        val scope = rememberCoroutineScope()

        if (sortedChapters.value.isEmpty()) {
            Text(stringResource(R.string.no_chapters))
            return
        }

        AnimatedVisibility(
            visible = firstCommentVisibility > 0,
            modifier = Modifier
                .zIndex(2f)
                .align(Alignment.BottomCenter),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .zIndex(2f)
                    .padding(10.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "Scroll to first comment",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            state = listState,
        ) {
            item {
                Row(
                    modifier = Modifier
                        .clickable {
                            changeSortType()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Icon(Icons.Outlined.SwapVert, "Swap sorting icon")
                    Text(stringResource(R.string.chapters_sort))
                }
            }
            items(
                items = sortedChapters.value,
                key = {
                    it.uuid
                },
            ) {chapter ->
                ChaptersListRow(
                    chapter = chapter,
                    onNavigateToReader = onNavigateToReader,
                    onNavigateToPlayer = onNavigateToPlayer,
                    onNavigateToLoginPage = onNavigateToLoginPage,
                    isAuth = isAuth,
                )
            }
        }
    }
}
