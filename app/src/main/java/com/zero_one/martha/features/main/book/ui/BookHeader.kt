package com.zero_one.martha.features.main.book.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zero_one.martha.R
import com.zero_one.martha.data.domain.model.Book

@Composable
fun BookHeader(
    book: Book,
    folderName: String,
    onOpenBottomSheet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(book.cover)
                .crossfade(true)
                .build(),
            contentDescription = "Book ${book.id} cover",
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_no_cover),
            modifier = Modifier
                .padding(
                    bottom = 10.dp,
                )
                .height(250.dp)
                .width(200.dp)
                .clip(shape = RoundedCornerShape(10.dp)),
        )
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleLarge,
        )
        Button(
            onClick = onOpenBottomSheet,
        ) {
            Text(folderName.ifEmpty {"Add"})
        }
    }
}
