package com.zero_one.martha.features.main.catalog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearPicker(
    minValue: Int,
    maxValue: Int,
    yearCurrentValue: Pair<Int, Int>,
    onValueChangeFinished: (start: Int, end: Int) -> Unit
) {
    var sliderPosition by remember {mutableStateOf(yearCurrentValue.first.toFloat() .. yearCurrentValue.second.toFloat())}
    val valueRange by remember {mutableStateOf(minValue.toFloat() .. maxValue.toFloat())}
    var start by remember {mutableIntStateOf(minValue)}
    var end by remember {mutableIntStateOf(maxValue)}

    Column {
        RangeSlider(
            value = sliderPosition,
            onValueChange = {range ->
                sliderPosition = range
            },
            onValueChangeFinished = {
                start = String.format(Locale.US, "%.0f", sliderPosition.start).toInt()
                end = String.format(Locale.US, "%.0f", sliderPosition.endInclusive).toInt()
                onValueChangeFinished(start, end)
            },
            valueRange = valueRange,
            startThumb = {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(25.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        )
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = CircleShape,
                        ),
                )
            },
            endThumb = {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(25.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        )
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = CircleShape,
                        ),
                )
            },
            track = {rangeSliderState ->
                SliderDefaults.Track(
                    rangeSliderState = rangeSliderState,
                    thumbTrackGapSize = 5.dp,
                    modifier = Modifier
                        .height(7.dp),
                    drawStopIndicator = {},
                )
            },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = String.format(Locale.US, "%.0f", sliderPosition.start),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = String.format(Locale.US, "%.0f", sliderPosition.endInclusive),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
