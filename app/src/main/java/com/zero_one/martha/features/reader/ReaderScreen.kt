package com.zero_one.martha.features.reader

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Toc
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.zero_one.martha.ui.components.CustomTopBarWithDropdownMenu

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    onNavigateToBack: () -> Unit,
    onNavigateToReader: (bookId: UInt, chapterId: UInt) -> Unit
) {
    BackHandler(
        onBack = {
            viewModel.destroy()
            onNavigateToBack()
        },
    )

    val reader = viewModel.reader.collectAsState()
    val pages = viewModel.pages.collectAsState()

    var menuState by remember {mutableStateOf(false)}
    var chaptersExpanded by remember {mutableStateOf(false)}
    var isCounting by remember {mutableStateOf(true)}
    var currentPage by remember {mutableStateOf("")}
    var endLineIndex by remember {mutableIntStateOf(0)}

    LaunchedEffect(reader.value) {
        Log.d("Reader value update", "Reader value update")
        if (reader.value != null) {
            currentPage = reader.value!!.replace("\n\n", "\n")
        }
        isCounting = true
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
        topBar = {
            AnimatedVisibility(menuState) {
                CustomTopBarWithDropdownMenu(
                    toSurfaceColor = true,
                    onNavigateToBack = {
                        viewModel.destroy()
                        onNavigateToBack()
                    },
                    title = if (viewModel.currentChapter != null)
                        viewModel.currentChapter!!.title
                    else "",
                    content = {
                        IconButton(
                            onClick = {
                                chaptersExpanded = !chaptersExpanded
                            },
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.Toc,
                                contentDescription = "More chapters button",
                            )
                        }
                        DropdownMenu(
                            expanded = chaptersExpanded,
                            onDismissRequest = {
                                chaptersExpanded = false
                            },
                        ) {
                            if (viewModel.book == null) {
                                CircularProgressIndicator()
                            } else {
                                viewModel.book!!.chapters.sortedBy {it.serial}.forEach {chapter ->
                                    if (chapter.text != "") {
                                        DropdownMenuItem(
                                            text = {
                                                Text(chapter.title)
                                            },
                                            onClick = {
                                                viewModel.destroy()
                                                onNavigateToBack()
                                                onNavigateToReader(viewModel.book!!.id, chapter.id)
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    },
                )
            }
        },
    ) {paddingValues ->
        Box(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    top = 32.dp,
                )
                .fillMaxSize(),
        ) {
            if (viewModel.bufferedReader == null) {
                CircularProgressIndicator()
                return@Box
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
            val interactionSource = remember {MutableInteractionSource()}
            viewModel.clearReader()

            LaunchedEffect(pagerState.currentPage) {
                menuState = false
            }

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
                        )
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                        ) {
                            menuState = !menuState
                        },
                )
            }
        }
    }
}

fun spToInt(value: TextUnit): Int = value.toString().replace(".sp", "").toFloat().toInt()

fun getMaxLines(height: TextUnit, fontSize: Int): Int {
    return spToInt(height) / fontSize.plus(2) - 1
}
