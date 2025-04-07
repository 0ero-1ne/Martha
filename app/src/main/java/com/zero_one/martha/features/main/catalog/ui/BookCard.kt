package com.zero_one.martha.features.main.catalog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zero_one.martha.data.domain.model.Book

@Composable
fun BookCard(
    book: Book,
    onBookClick: (bookId: UInt) -> Unit
) {
    Box {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(
                        bottom = 5.dp,
                    )
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.Green)
                    .clickable {
                        onBookClick(book.id)
                    },
            )
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                minLines = 1,
            )
        }
    }
}
