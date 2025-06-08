package com.zero_one.martha.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
fun CustomTabRow(
    pagerState: PagerState,
    tabs: Set<String>,
    modifier: Modifier = Modifier
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

    TabRow(
        selectedTabIndex = selectedTabIndex.value,
        modifier = modifier
            .fillMaxWidth(),
        indicator = {tabPositions ->
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier
                    .smoothTabIndicatorOffset(
                        previousTabPosition = tabPositions[previousTabIndex],
                        newTabPosition = tabPositions[targetTabIndex],
                        swipeProgress = pagerState.currentPageOffsetFraction,
                    ),
            )
        },
    ) {
        tabs.forEachIndexed {index, title ->
            Tab(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                        ),
                    ),
                selected = selectedTabIndex.value == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                unselectedContentColor = MaterialTheme.colorScheme.outline,
                text = {
                    Text(
                        text = parseSystemFolderName(title),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
            )
        }
    }
}
