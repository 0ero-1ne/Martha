package com.zero_one.martha.features.main.book.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zero_one.martha.data.domain.model.Book

@Composable
fun BookHeader(
    book: Book
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(250.dp)
                .padding(
                    bottom = 10.dp,
                    top = 10.dp,
                )
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Green),
        )
        Text(
            modifier = Modifier
                .padding(
                    bottom = 10.dp,
                ),
            text = book.title,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
