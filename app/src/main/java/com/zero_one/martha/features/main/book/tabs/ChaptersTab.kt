package com.zero_one.martha.features.main.book.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zero_one.martha.data.domain.model.Chapter
import com.zero_one.martha.features.main.book.ui.ChaptersListRow

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

    if (sortedChapters.value.isEmpty()) {
        Text("No chapters...")
        return
    }

    Row(
        modifier = Modifier
            .clickable {
                changeSortType()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(Icons.Outlined.SwapVert, "Swap sorting icon")
        Text("Sort")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        state = listState,
    ) {
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
            if (sortedChapters.value.last() != chapter) {
                HorizontalDivider()
            }
        }
    }
}
