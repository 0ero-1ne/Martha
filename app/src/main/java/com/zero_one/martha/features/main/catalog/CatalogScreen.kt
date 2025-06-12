package com.zero_one.martha.features.main.catalog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zero_one.martha.R
import com.zero_one.martha.features.main.catalog.ui.BookCard
import com.zero_one.martha.features.main.catalog.ui.CustomSearchBar
import com.zero_one.martha.features.main.catalog.ui.FiltersMenu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    onBookClick: (bookId: UInt) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {paddingValues ->
        val books = viewModel.books.collectAsState()
        val filteredBooks = viewModel.filteredBooks.collectAsState()
        val tags = viewModel.tags.collectAsState()
        val tagFilters = viewModel.tagFilters.collectAsState()
        val statusFilters = viewModel.statusFilters.collectAsState()
        var query by remember {mutableStateOf("")}

        var showFiltersMenu by remember {mutableStateOf(false)}
        val filterMenuState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        Column(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                ),
        ) {
            if (showFiltersMenu) {
                FiltersMenu(
                    tags = tags.value,
                    onDismiss = {
                        showFiltersMenu = false
                    },
                    onAddTagFilter = viewModel::onAddTagFilter,
                    onRemoveTagFilter = viewModel::onRemoveTagFilter,
                    onAddStatusFilter = viewModel::onAddStatusFilter,
                    onRemoveStatusFilter = viewModel::onRemoveStatusFilter,
                    tagFilters = tagFilters,
                    statusFilters = statusFilters,
                    onApplyFilters = {
                        viewModel.search(query)
                    },
                    state = filterMenuState,
                    yearsLimits = Pair(
                        first = books.value!!.minBy {it.year}.year,
                        second = books.value!!.maxBy {it.year}.year,
                    ),
                    yearCurrentValue = Pair(
                        first = viewModel.startYear,
                        second = viewModel.endYear,
                    ),
                    onSetYearFilter = viewModel::onSetYearFilter,
                )
            }

            CustomSearchBar(
                onSearch = viewModel::search,
                query = query,
                onValueChange = {
                    query = it
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.dp,
                        start = 5.dp,
                        end = 5.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            viewModel.columns++

                            if (viewModel.columns > 4) {
                                viewModel.columns = 2
                            }
                        },
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Change grid cells count",
                        modifier = Modifier.padding(end = 5.dp),
                    )
                    Text("${stringResource(R.string.tiles_title)}: ${viewModel.columns}")
                }
                Box(modifier = Modifier.width(10.dp))
                Row(
                    modifier = Modifier
                        .clickable {
                            showFiltersMenu = true
                        },
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterAlt,
                        contentDescription = "Open filter menu",
                        modifier = Modifier.padding(end = 5.dp),
                    )
                    Text(stringResource(R.string.filters_title))
                }
            }

            if (books.value == null && filteredBooks.value == null) {
                CircularProgressIndicator()
                return@Column
            }

            if (viewModel.searching) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
                return@Scaffold
            }

            if (books.value!!.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(stringResource(R.string.no_books_by_query))
                }
                return@Scaffold
            }

            val listState = rememberLazyGridState()
            val scope = rememberCoroutineScope()
            val firstBookVisibility by remember {
                derivedStateOf {listState.firstVisibleItemIndex}
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Column(
                    Modifier
                        .clip(CircleShape)
                        .wrapContentSize()
                        .align(Alignment.BottomCenter)
                        .zIndex(2f)
                        .clickable {
                            scope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                ) {
                    AnimatedVisibility(
                        visible = firstBookVisibility > 0,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    CircleShape,
                                )
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape,
                                )
                                .zIndex(2f)
                                .padding(10.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "Scroll to first book",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }

                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Fixed(viewModel.columns),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (filteredBooks.value != null) {
                        items(filteredBooks.value!!, key = {it.uuid}) {book ->
                            BookCard(
                                book = book,
                                onBookClick = onBookClick,
                                height = getBookSize(viewModel.columns),
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getBookSize(columns: Int): Dp {
    return when (columns) {
        2 -> 250.dp
        3 -> 190.dp
        4 -> 150.dp
        else -> 180.dp
    }
}
