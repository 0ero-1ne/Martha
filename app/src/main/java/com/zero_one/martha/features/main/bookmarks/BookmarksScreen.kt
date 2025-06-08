package com.zero_one.martha.features.main.bookmarks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.features.main.bookmarks.components.BottomSheet
import com.zero_one.martha.features.main.bookmarks.components.SavedBookItem
import com.zero_one.martha.modifier.bottomBorder
import com.zero_one.martha.ui.components.CustomScrollableTabRow
import kotlinx.coroutines.launch

@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel,
    onNavigateToLoginPage: () -> Unit,
    onNavigateToBook: (bookId: UInt) -> Unit,
) {
    var showBottomSheet by remember {mutableStateOf(false)}
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                )
                .fillMaxSize(),
        ) {
            val user = viewModel.user.collectAsState(User())
            val books = viewModel.books.collectAsState()

            if (user.value.id == 0u) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding() + 16.dp,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "You are not authorized",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                            ),
                        text = "Authorize to see bookmarks",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    OutlinedButton(
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                            ),
                        onClick = onNavigateToLoginPage,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    ) {
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                return@Scaffold
            }

            val bookmarks = viewModel.bookmarks.collectAsState()
            val scope = rememberCoroutineScope()

            if (bookmarks.value == null) {
                CircularProgressIndicator()
                return@Column
            }

            var userId by rememberSaveable {mutableStateOf("")}
            val pageCount = remember {mutableIntStateOf(bookmarks.value!!.size)}
            val pagerState = rememberPagerState {pageCount.intValue}
            var pages by remember {mutableStateOf(bookmarks.value!!.keys)}

            LaunchedEffect(bookmarks.value!!) {
                pageCount.intValue = bookmarks.value!!.size
                pages = bookmarks.value!!.keys
            }

            LaunchedEffect(user.value) {
                if (userId == "") {
                    userId = user.value.id.toString()
                }

                if (user.value.id.toString() != userId && user.value.id != 0u) {
                    userId = user.value.id.toString()
                    pagerState.animateScrollToPage(0)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 16.dp,
                    )
                    .bottomBorder(
                        color = MaterialTheme.colorScheme.primary,
                        height = 1f,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CustomScrollableTabRow(
                    modifier = Modifier
                        .weight(5f),
                    pagerState = pagerState,
                    tabs = pages,
                    initFolderName = viewModel.initFolderName,
                )

                IconButton(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        showBottomSheet = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Folder control",
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    ),
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize(),
                    pageSpacing = 5.dp,
                ) {page ->
                    val columns by remember {mutableIntStateOf(3)}
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        val folderString = bookmarks.value!!.keys.elementAt(page)
                        val savedBooks = bookmarks.value!![folderString]!!

                        if (savedBooks.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "No saved books in folder",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                            return@HorizontalPager
                        }

                        val listState = rememberLazyListState()
                        val firstBookVisibility by remember {
                            derivedStateOf {listState.firstVisibleItemIndex}
                        }

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
                            columns = GridCells.Fixed(columns),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(bookmarks.value!![folderString]!!) {savedBook ->
                                val book = books.value.firstOrNull {it.id == savedBook.bookId}
                                SavedBookItem(
                                    book = book,
                                    savedBook = savedBook,
                                    size = getBookSize(columns),
                                    onBookClick = {
                                        onNavigateToBook(book!!.id)
                                    },
                                    onDeleteBookmark = viewModel::onDeleteBookmark,
                                )
                            }
                        }
                    }
                }

                if (showBottomSheet) {
                    BottomSheet(
                        pages = pages,
                        currentTab = pagerState.currentPage,
                        onDismiss = {
                            showBottomSheet = false
                        },
                        onAddNewFolder = viewModel::addNewFolder,
                        onDeleteFolder = viewModel::deleteFolder,
                        onScrollToFirstTab = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                    )
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
