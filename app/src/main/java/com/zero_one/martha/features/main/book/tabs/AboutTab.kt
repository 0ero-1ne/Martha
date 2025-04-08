package com.zero_one.martha.features.main.book.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Row(
        modifier = Modifier
            .padding(
                top = 10.dp,
            ),
    ) {
        book.tags.forEach {tag ->
            Text(
                modifier = Modifier
                    .padding(
                        end = 10.dp,
                    )
                    .background(Color.DarkGray),
                text = "#" + tag.title,
            )
        }
    }
    Text(
        modifier = Modifier
            .padding(
                bottom = 5.dp,
                top = 10.dp,
            ),
        text = "Authors",
        style = MaterialTheme.typography.titleLarge,
    )
    Row {
        book.authors.forEach {author ->
            Text(
                modifier = Modifier
                    .padding(
                        end = 10.dp,
                    )
                    .background(Color.DarkGray),
                text = author.fullname,
            )
        }
    }
}
