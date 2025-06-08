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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zero_one.martha.R
import com.zero_one.martha.data.domain.model.SavedBook
import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.features.main.book.tabs.AboutTab
import com.zero_one.martha.features.main.book.tabs.ChaptersTab
import com.zero_one.martha.features.main.book.tabs.CommentsTab
import com.zero_one.martha.features.main.book.ui.BookHeader
import com.zero_one.martha.features.main.book.ui.BottomSheetBookmarks
import com.zero_one.martha.features.main.book.ui.BottomSheetRating
import com.zero_one.martha.ui.components.CustomTabRow
import com.zero_one.martha.ui.components.CustomTopBar

@Composable
fun BookScreen(
    viewModel: BookViewModel,
    onNavigateToBack: () -> Unit,
    onNavigateToReader: (bookId: UInt, chapterId: UInt) -> Unit,
    onNavigateToPlayer: (bookId: UInt, chapterId: UInt) -> Unit,
    onNavigateToLoginPage: () -> Unit
) {
    val user = viewModel.user.collectAsState(User())
    val comments = viewModel.comments.collectAsState()

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
            val pagerState = rememberPagerState(
                pageCount = {
                    3
                },
            )
            val tabs = listOf(
                stringResource(R.string.pager_about_tab),
                stringResource(R.string.pager_chapters_tab),
                stringResource(R.string.pager_comments_tab),
            )

            if (viewModel.book == null) {
                CircularProgressIndicator()
                return@Scaffold
            }

            if (viewModel.book!!.id == 0u) {
                Text("Read book info error")
                return@Scaffold
            }

            LaunchedEffect(user.value) {
                val bookmarks = user.value.savedBooks
                var folderName = ""
                bookmarks.forEach {(key, value) ->
                    if (value.any {it.bookId == viewModel.book!!.id}) {
                        folderName = key
                    }
                }
                viewModel.bookmarkFolderName = folderName
            }

            var showBookmarksModal by remember {mutableStateOf(false)}
            var showRatingModal by remember {mutableStateOf(false)}
            val userBookRate = viewModel.userBookRate.collectAsState()

            BookHeader(
                book = viewModel.book!!,
                folderName = viewModel.bookmarkFolderName,
                userRating = userBookRate.value.rating,
                onOpenBookmarksModal = {
                    showBookmarksModal = true
                },
                onOpenRatingModal = {
                    showRatingModal = true
                },
                isAuth = viewModel::isAuth,
                onNavigateToReader = onNavigateToReader,
                onNavigateToPlayer = onNavigateToPlayer,
                onNavigateToLoginPage = onNavigateToLoginPage,
                savedBook = user.value.savedBooks[viewModel.bookmarkFolderName]?.firstOrNull {
                    it.bookId == viewModel.book!!.id
                } ?: SavedBook(),
                chapters = viewModel.chapters ?: emptyList(),
            )

            CustomTabRow(
                pagerState = pagerState,
                tabs = tabs.toSet(),
                modifier = Modifier,
                // .clip(RoundedCornerShape(10.dp))
                // .border(
                //     1.dp,
                //     MaterialTheme.colorScheme.primary,
                //     RoundedCornerShape(10.dp),
                // ),
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = pagerState,
                pageSpacing = 16.dp,
            ) {page ->
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
                        comments = comments.value,
                        onSaveComment = viewModel::saveComment,
                        onUpdateComment = viewModel::updateComment,
                        onDeleteComment = viewModel::deleteComment,
                        onRateComment = viewModel::onRateComment,
                        onNavigateToLoginPage = onNavigateToLoginPage,
                        commentEvents = viewModel.commentValidationEvents,
                        isAuth = viewModel::isAuth,
                        userId = user.value.id,
                        userRole = user.value.role,
                    )
                }
            }

            if (showBookmarksModal) {
                BottomSheetBookmarks(
                    onDismiss = {
                        showBookmarksModal = false
                    },
                    bookmarks = user.value.savedBooks.keys,
                    onSaveBookInBookmarks = viewModel::saveBookInBookmarks,
                    onRemoveBookFromBookmarks = viewModel::onRemoveBookFromBookmarks,
                    onReplaceBookmark = viewModel::onReplaceBookmark,
                    currentFolderName = viewModel.bookmarkFolderName,
                )
            }

            if (showRatingModal) {
                BottomSheetRating(
                    bookId = viewModel.book!!.id,
                    onDismiss = {
                        showRatingModal = false
                    },
                    bookRate = userBookRate.value,
                    onRateBook = viewModel::onRateBook,
                    isAuth = viewModel::isAuth,
                )
            }
        }
    }
}
