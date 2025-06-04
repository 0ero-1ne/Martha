package com.zero_one.martha.features.main.book.tabs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.zero_one.martha.data.domain.model.Book

@Composable
fun AboutTab(
    book: Book
) {
    Text(
        modifier = Modifier
            .padding(
                bottom = 5.dp,
            ),
        text = "Description",
        style = MaterialTheme.typography.titleLarge,
    )
    Text(
        text = book.description,
        style = MaterialTheme.typography.bodyMedium,
    )
    Text(
        modifier = Modifier
            .padding(
                bottom = 5.dp,
                top = 10.dp,
            ),
        text = "Tags",
        style = MaterialTheme.typography.titleLarge,
    )
    FlowRow(
        modifier = Modifier
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        book.tags.forEach {tag ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(10.dp),
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            top = 6.dp,
                            bottom = 6.dp,
                            start = 12.dp,
                            end = 12.dp,
                        ),
                    text = tag.title,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
