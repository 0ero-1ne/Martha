package com.zero_one.martha.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.zero_one.martha.modifier.smoothTabIndicatorOffset
import com.zero_one.martha.utils.parseSystemFolderName
import kotlinx.coroutines.launch

@Composable
fun CustomScrollableTabRow(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tabs: Set<String>,
    initFolderName: String
) {
    val scope = rememberCoroutineScope()
    var previousTabIndex by rememberSaveable {mutableIntStateOf(0)}
    var targetTabIndex by rememberSaveable {mutableIntStateOf(0)}
    val selectedTabIndex = remember {
        derivedStateOf {pagerState.currentPage}
    }

    LaunchedEffect(pagerState.currentPageOffsetFraction) {
        val scrollFraction = pagerState.currentPageOffsetFraction
        if (scrollFraction > 0) {
            previousTabIndex = pagerState.currentPage
            targetTabIndex = previousTabIndex + 1
        }
        if (scrollFraction < 0) {
            previousTabIndex = pagerState.currentPage
            targetTabIndex = previousTabIndex - 1
        }
    }

    LaunchedEffect(tabs) {
        if (tabs.isNotEmpty() && initFolderName.isNotEmpty()) {
            scope.launch {
                pagerState.animateScrollToPage(tabs.indexOf(initFolderName))
            }
        }
    }

    if (tabs.isNotEmpty())
        ScrollableTabRow(
            edgePadding = 0.dp,
            selectedTabIndex = selectedTabIndex.value,
            modifier = modifier,
            indicator = {tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .smoothTabIndicatorOffset(
                            previousTabPosition = tabPositions[previousTabIndex],
                            newTabPosition = tabPositions[targetTabIndex],
                            swipeProgress = pagerState.currentPageOffsetFraction,
                        ),
                )
            },
            divider = {},
        ) {
            tabs.forEachIndexed {index, title ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    text = {
                        Text(parseSystemFolderName(title))
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp)),
                )
            }
        }
}
