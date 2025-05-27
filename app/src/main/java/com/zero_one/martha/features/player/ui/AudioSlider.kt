package com.zero_one.martha.features.player.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSlider(
    value: Long,
    onValueChange: (newValue: Float) -> Unit,
    totalDuration: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(0.95f),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(value.toTime())
        Text(totalDuration.toTime())
    }

    Slider(
        modifier = modifier
            .fillMaxWidth(),
        value = value.toFloat(),
        onValueChange = {
            onValueChange(it)
        },
        valueRange = 0f .. totalDuration.toFloat(),
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.onSurface,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.onPrimary,
            inactiveTickColor = Color.Transparent,
        ),
        steps = 0,
        thumb = {
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
        track = {sliderState ->
            SliderDefaults.Track(
                sliderState = sliderState,
                thumbTrackGapSize = 5.dp,
                modifier = Modifier
                    .height(7.dp),
                drawStopIndicator = {},
            )
        },
    )
}

@SuppressLint("DefaultLocale")
fun Long.toTime(): String {
    val stringBuffer = StringBuffer()

    val minutes = (this / 60000).toInt()
    val seconds = (this % 60000 / 1000).toInt()

    stringBuffer
        .append(String.format("%02d", minutes))
        .append(":")
        .append(String.format("%02d", seconds))

    return stringBuffer.toString()
}
