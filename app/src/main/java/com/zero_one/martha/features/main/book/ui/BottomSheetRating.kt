package com.zero_one.martha.features.main.book.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zero_one.martha.data.domain.model.BookRate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetRating(
    bookId: UInt,
    bookRate: BookRate,
    onDismiss: () -> Unit,
    onRateBook: (bookId: UInt, rating: Int) -> Unit,
    isAuth: () -> Boolean
) {
    val sheetState = rememberModalBottomSheetState()
    var rating by remember {mutableIntStateOf(bookRate.rating)}
    val interactionSource = remember {MutableInteractionSource()}

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "Leave a rate",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(bottom = 10.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (i in 1 .. 5) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Default.Star
                        else Icons.Default.StarBorder,
                        contentDescription = "Star icon button",
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                            ) {
                                rating = if (rating == i) 0 else i
                            }
                            .size(60.dp)
                            .padding(horizontal = 5.dp),
                    )
                }
            }
            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(56.dp)
                    .fillMaxWidth(),
                enabled = isAuth(),
                onClick = {
                    if (bookRate.rating == rating) {
                        onDismiss()
                        return@Button
                    }
                    onRateBook(bookId, rating)
                    onDismiss()
                },
                shape = RoundedCornerShape(5.dp),
            ) {
                Text(
                    text = "Rate",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
