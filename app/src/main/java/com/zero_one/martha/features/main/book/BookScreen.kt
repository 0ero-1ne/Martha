package com.zero_one.martha.features.main.book

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zero_one.martha.ui.components.CustomTopBar

@Composable
fun BookScreen(
    viewModel: BookViewModel,
    onNavigateToBack: () -> Unit
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
            if (viewModel.book == null) {
                CircularProgressIndicator()
                return@Scaffold
            }

            if (viewModel.book!!.id == 0u) {
                Text("Read book info error")
                return@Scaffold
            }

            Text(viewModel.book!!.title)

            if (viewModel.chapters == null) {
                CircularProgressIndicator()
                return@Scaffold
            }

            if (viewModel.chapters!!.isEmpty()) {
                Text("No chapters")
                return@Scaffold
            }

            viewModel.chapters!!.forEach {chapter ->
                Text(chapter.title)
            }
        }
    }
}
