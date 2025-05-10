package com.zero_one.martha.modifier

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.TabPosition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.IntOffset
import kotlin.math.abs

fun Modifier.smoothTabIndicatorOffset(
    previousTabPosition: TabPosition,
    newTabPosition: TabPosition,
    swipeProgress: Float
): Modifier = composed {
    val currentTabWidth =
        previousTabPosition.width + (newTabPosition.width - previousTabPosition.width) * abs(
            swipeProgress,
        )
    val indicatorOffset =
        previousTabPosition.left + (newTabPosition.left - previousTabPosition.left) * abs(
            swipeProgress,
        )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset {IntOffset(x = indicatorOffset.roundToPx(), y = 0)}
        .width(currentTabWidth)
}
