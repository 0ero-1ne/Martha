package com.zero_one.martha.features.main.book

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.features.main.book.tabs.AboutTab
import com.zero_one.martha.features.main.book.tabs.ChaptersTab
import com.zero_one.martha.features.main.book.tabs.CommentsTab
import com.zero_one.martha.features.main.book.ui.BookHeader
import com.zero_one.martha.features.main.book.ui.BottomSheetBookmarks
import com.zero_one.martha.ui.components.CustomTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    viewModel: BookViewModel,
    onNavigateToBack: () -> Unit,
    onNavigateToReader: (bookId: UInt, chapterId: UInt) -> Unit,
    onNavigateToPlayer: (bookId: UInt, chapterId: UInt) -> Unit,
    onNavigateToLoginPage: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onNavigateToBack = onNavigateToBack,
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp,
                ),
            )
        },
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                ),
        ) {
            val scope = rememberCoroutineScope()
            val pagerState = rememberPagerState(
                pageCount = {
                    3
                },
            )
            val selectedTabIndex = remember {
                derivedStateOf {pagerState.currentPage}
            }
            val tabs = listOf("About", "Chapters", "Comments")

            if (viewModel.book == null) {
                CircularProgressIndicator()
                return@Scaffold
            }

            if (viewModel.book!!.id == 0u) {
                Text("Read book info error")
                return@Scaffold
            }

            val user = viewModel.user.collectAsState(User())
            var showBottomSheet by remember {mutableStateOf(false)}

            BookHeader(
                book = viewModel.book!!,
                folderName = viewModel.bookmarkFolderName,
                onOpenBottomSheet = {
                    showBottomSheet = true
                },
                isAuth = viewModel::isAuth,
            )

            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex.value,
                modifier = Modifier.fillMaxWidth(),
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(selectedTabIndex = selectedTabIndex.value),
                    )
                },
            ) {
                tabs.forEachIndexed {index, title ->
                    Tab(
                        selected = selectedTabIndex.value == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(title)
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = pagerState,
            ) {page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    when (page) {
                        0 -> AboutTab(
                            book = viewModel.book!!,
                        )

                        1 -> ChaptersTab(
                            chapters = viewModel.chapters!!,
                            sortType = viewModel.chaptersSortType,
                            changeSortType = {
                                viewModel.changeSortType()
                            },
                            onNavigateToReader = onNavigateToReader,
                            onNavigateToPlayer = onNavigateToPlayer,
                            onNavigateToLoginPage = onNavigateToLoginPage,
                            isAuth = viewModel::isAuth,
                        )

                        2 -> CommentsTab(
                            comments = viewModel.comments,
                            onSaveComment = viewModel::saveComment,
                            commentEvents = viewModel.commentValidationEvents,
                        )
                    }
                }
            }

            if (showBottomSheet) {
                BottomSheetBookmarks(
                    onDismiss = {
                        showBottomSheet = false
                    },
                    bookmarks = user.value.savedBooks.keys,
                    onSaveBookInBookmarks = viewModel::saveBookInBookmarks,
                    onRemoveBookFromBookmarks = viewModel::onRemoveBookFromBookmarks,
                    currentFolderName = viewModel.bookmarkFolderName,
                )
            }
        }
    }
}
