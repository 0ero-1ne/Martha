package com.zero_one.martha.features.main.catalog.ui

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zero_one.martha.BuildConfig
import com.zero_one.martha.R
import com.zero_one.martha.data.domain.model.Book

@Composable
fun BookCard(
    book: Book,
    onBookClick: (bookId: UInt) -> Unit,
    height: Dp
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("${BuildConfig.STORAGE_URL}images/${book.cover}")
                    .crossfade(true)
                    .build(),
                contentDescription = "Book ${book.id} cover",
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_no_cover),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .padding(
                        bottom = 5.dp,
                    )
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        keyboardController?.hide()
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
