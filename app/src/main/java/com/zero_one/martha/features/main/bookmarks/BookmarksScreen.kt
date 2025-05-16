package com.zero_one.martha.features.main.bookmarks

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.features.main.bookmarks.components.BottomSheet
import com.zero_one.martha.ui.components.CustomScrollableTabRow
import kotlinx.coroutines.launch

@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    bottom = paddingValues.calculateBottomPadding(),
                )
                .fillMaxSize(),
        ) {
            val user = viewModel.user.collectAsState(User())

            if (user.value.id == 0u) {
                Text("No user")
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
            var showBottomSheet by remember {mutableStateOf(false)}
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

            CustomScrollableTabRow(
                pagerState = pagerState,
                tabs = pages,
            )

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = {
                    showBottomSheet = true
                },
            ) {
                Text("Open")
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.White),
            ) {page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                ) {
                    val folderString = bookmarks.value!!.keys.elementAt(page)
                    val bookIds = bookmarks.value!![folderString]!!.joinToString(", ")
                    Text(bookmarks.value!!.keys.elementAt(page))
                    Text(bookIds)
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
