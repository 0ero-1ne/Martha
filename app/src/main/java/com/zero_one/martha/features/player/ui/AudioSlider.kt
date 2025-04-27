package com.zero_one.martha.features.player.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AudioSlider(
    value: Long,
    onValueChange: (newValue: Float) -> Unit,
    totalDuration: Long
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 10.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(value.toTime())
        Text(totalDuration.toTime())
    }
    Slider(
        value = value.toFloat(),
        onValueChange = {
            onValueChange(it)
        },
        valueRange = 0f .. totalDuration.toFloat(),
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.onSurface,
            activeTrackColor = Color.DarkGray,
            inactiveTrackColor = Color.Gray,
        ),
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
