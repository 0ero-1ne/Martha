package com.zero_one.martha.features.reader

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
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
    val reader = viewModel.reader.collectAsState()

    var isCounting = remember {true}
    var pages = remember {mutableListOf<String>()}
    var currentPage = remember {mutableStateOf("")}
    var currentPageNumber = remember {mutableIntStateOf(0)}
    val endLineIndex = remember {mutableIntStateOf(0)}

    LaunchedEffect(reader.value) {
        if (reader.value != null) {
            currentPage.value = reader.value!!.replace("\n\n", "\n")
        }
    }

    LaunchedEffect(
        endLineIndex.intValue,
    ) {
        if (reader.value != null) {
            val newPage = currentPage.value.substring(0, endLineIndex.intValue)
            if (newPage.isNotEmpty()) {
                pages.add(newPage.replace("\n\n", "\n"))
                Log.d("New page", pages.last())
            }

            currentPage.value =
                currentPage
                    .value
                    .substring(endLineIndex.intValue, currentPage.value.lastIndex + 1)
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
                    onNavigateToBack()
                },
            ) {
                Text("Back")
            }

            Text(
                isCounting.let {
                    if (!it) {
                        "Pages: ${pages.size}"
                    } else {
                        "Pages: ?"
                    }
                },
            )
            Text("Current page = ${currentPageNumber.intValue}")
            Button(
                onClick = {
                    if (currentPageNumber.intValue > 0) {
                        currentPageNumber.intValue--
                    }
                    currentPage.value = pages[currentPageNumber.intValue]
                },
            ) {
                Text("Prev")
            }
            Button(
                onClick = {
                    if (currentPageNumber.intValue < pages.size - 1) {
                        currentPageNumber.intValue++
                    }
                    currentPage.value = pages[currentPageNumber.intValue]
                },
            ) {
                Text("Next")
            }
            Button(
                enabled = reader.value != null,
                onClick = {
                    // viewModel.currentPage = reader.value!!.size - 1
                },
            ) {
                Text("Last page")
            }

            if (viewModel.bufferedReader == null) {
                CircularProgressIndicator()
                return@Column
            }

            Text(
                text = currentPage.value,
                style = textStyle,
                color = if (isCounting) Color.Transparent else Color.Unspecified,
                maxLines = if (viewModel.maxLines > 0) viewModel.maxLines else Int.MAX_VALUE,
                onTextLayout = {
                    if (it.didOverflowHeight || it.hasVisualOverflow) {
                        endLineIndex.intValue = it.getLineEnd(
                            lineIndex = viewModel.maxLines - 1,
                        )
                    } else {
                        if (currentPage.value.trim().isNotEmpty() && isCounting) {
                            pages.add(currentPage.value)
                            Log.d("New page", pages.last())
                            isCounting = false
                            currentPage.value = pages[currentPageNumber.intValue]
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.White)
                    .onGloballyPositioned {it ->
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
        }
    }
}

fun dpToInt(value: Dp): Int = value.toString().replace(".dp", "").toFloat().toInt()

fun spToInt(value: TextUnit): Int = value.toString().replace(".sp", "").toFloat().toInt()

fun getMaxLines(height: TextUnit, fontSize: Int): Int {
    return spToInt(height) / fontSize.plus(2) - 1
}
