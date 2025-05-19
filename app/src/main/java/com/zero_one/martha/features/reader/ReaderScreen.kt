package com.zero_one.martha.features.reader

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.zero_one.martha.features.reader.ui.readerTextStyle

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    onNavigateToBack: () -> Unit
) {
    BackHandler(
        onBack = {
            viewModel.destroy()
            onNavigateToBack()
        },
    )

    val reader = viewModel.reader.collectAsState()
    val pages = viewModel.pages.collectAsState()

    var isCounting by remember {mutableStateOf(true)}
    var currentPage by remember {mutableStateOf("")}
    var endLineIndex by remember {mutableIntStateOf(0)}

    LaunchedEffect(reader.value) {
        if (reader.value != null) {
            currentPage = reader.value!!.replace("\n\n", "\n")
        }
    }

    LaunchedEffect(
        endLineIndex,
    ) {
        if (reader.value != null) {
            val newPage = currentPage.substring(0, endLineIndex)
            if (newPage.isNotEmpty()) {
                viewModel.addPage(newPage.replace("\n\n", "\n"))
            }

            currentPage = currentPage.substring(endLineIndex, currentPage.lastIndex + 1)
        }
    }

    val localDensity = LocalDensity.current
    val textStyle = readerTextStyle(viewModel.fontSize.intValue)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                )
                .fillMaxSize(),
        ) {
            Button(
                onClick = {
                    viewModel.destroy()
                    onNavigateToBack()
                },
            ) {
                Text("Back")
            }

            if (viewModel.bufferedReader == null) {
                CircularProgressIndicator()
                return@Column
            }

            if (isCounting) {
                Text(
                    text = currentPage,
                    style = textStyle,
                    color = Color.Transparent,
                    maxLines = if (viewModel.maxLines > 0) viewModel.maxLines else Int.MAX_VALUE,
                    onTextLayout = {
                        if (it.didOverflowHeight || it.hasVisualOverflow) {
                            endLineIndex = it.getLineEnd(
                                lineIndex = viewModel.maxLines - 1,
                            )
                        } else {
                            if (currentPage.trim().isNotEmpty() && isCounting) {
                                viewModel.addPage(currentPage)
                                isCounting = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 10.dp,
                            start = 5.dp,
                            end = 5.dp,
                        )
                        .onGloballyPositioned {
                            if (viewModel.maxLines > 0) {
                                return@onGloballyPositioned
                            }

                            viewModel.maxLines = with(localDensity) {
                                getMaxLines(it.size.height.toSp(), viewModel.fontSize.intValue)
                            }

                            if (viewModel.maxLines > 0) {
                                viewModel.loadPages()
                            }
                        },
                )
                return@Scaffold
            }

            val pagerState = rememberPagerState(
                pageCount = {
                    pages.value.size
                },
                initialPage = viewModel.currentPage,
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 10.dp,
                    ),
            ) {page ->
                viewModel.currentPage = pagerState.currentPage
                Text(
                    text = pages.value[page],
                    style = textStyle,
                    color = Color.Unspecified,
                    maxLines = viewModel.maxLines,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 5.dp,
                            end = 5.dp,
                        ),
                )
            }
        }
    }
}

fun spToInt(value: TextUnit): Int = value.toString().replace(".sp", "").toFloat().toInt()

fun getMaxLines(height: TextUnit, fontSize: Int): Int {
    return spToInt(height) / fontSize.plus(2) - 1
}
