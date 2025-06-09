package com.zero_one.martha.features.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zero_one.martha.R
import com.zero_one.martha.features.main.home.components.BookItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToBook: (UInt) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    bottom = paddingValues.calculateBottomPadding(),
                    top = 16.dp,
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            val popularBooks by viewModel.popularBooks.collectAsState()
            val newBooks by viewModel.newBooks.collectAsState()
            val forYouBooks by viewModel.forYouBooks.collectAsState()

            Text(
                text = stringResource(R.string.popular_now).uppercase(),
                style = MaterialTheme.typography.titleLarge,
            )

            if (popularBooks == null) {
                CircularProgressIndicator()
            } else {
                LazyRow(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            bottom = 24.dp,
                        ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        popularBooks!!.sortedByDescending {book ->
                            val rating =
                                book.rates.sumOf {it.rating}.toFloat() / book.rates.size.toFloat()
                            if (rating.isNaN()) {
                                0f
                            } else {
                                rating
                            }
                        }.take(5),
                        key = {it.uuid},
                    ) {book ->
                        BookItem(
                            book = book,
                            onBookClick = onNavigateToBook,
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.new_books).uppercase(),
                style = MaterialTheme.typography.titleLarge,
            )

            if (newBooks == null) {
                CircularProgressIndicator()
            } else {
                LazyRow(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            bottom = 16.dp,
                        ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(newBooks!!.sortedByDescending {it.id}.take(5), key = {it.uuid}) {book ->
                        BookItem(
                            book = book,
                            onBookClick = onNavigateToBook,
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.books_for_you).uppercase(),
                style = MaterialTheme.typography.titleLarge,
            )

            if (forYouBooks == null) {
                CircularProgressIndicator()
            } else {
                LazyRow(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            bottom = 16.dp,
                        ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(forYouBooks!!.shuffled().take(5), key = {it.uuid}) {book ->
                        BookItem(
                            book = book,
                            onBookClick = onNavigateToBook,
                        )
                    }
                }
            }
        }
    }
}
