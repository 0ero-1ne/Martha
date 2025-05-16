package com.zero_one.martha.features.main.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zero_one.martha.features.main.catalog.ui.BookCard
import com.zero_one.martha.features.main.catalog.ui.CustomSearchBar

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    onBookClick: (bookId: UInt) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {paddingValues ->
        val books = viewModel.books.collectAsState()
        Column(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                ),
        ) {
            CustomSearchBar(
                onSearch = viewModel::search,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.dp,
                        start = 5.dp,
                        end = 5.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            viewModel.columns++

                            if (viewModel.columns > 4) {
                                viewModel.columns = 2
                            }
                        },
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Change grid cells count",
                        modifier = Modifier.padding(end = 5.dp),
                    )
                    Text("Tiles: ${viewModel.columns}")
                }
            }

            if (books.value == null) {
                CircularProgressIndicator()
                return@Column
            }

            if (viewModel.searching) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
                return@Scaffold
            }

            if (books.value!!.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("No books by query")
                }
                return@Scaffold
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(viewModel.columns),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(books.value!!, key = {it.uuid}) {book ->
                    BookCard(
                        book = book,
                        onBookClick = onBookClick,
                        height = getBookSize(viewModel.columns),
                    )
                }
            }
        }
    }
}

private fun getBookSize(columns: Int): Dp {
    return when (columns) {
        2 -> 250.dp
        3 -> 190.dp
        4 -> 150.dp
        else -> 180.dp
    }
}
