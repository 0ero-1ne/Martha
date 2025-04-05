package com.zero_one.martha.features.main.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zero_one.martha.features.main.catalog.ui.BookCard
import com.zero_one.martha.features.main.catalog.ui.CustomSearchBar

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel
) {
    if (viewModel.books == null) {
        CircularProgressIndicator()
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                ),
        ) {
            CustomSearchBar(
                onSearch = {},
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(viewModel.books!!.size) {i ->
                    BookCard(
                        book = viewModel.books!![i],
                    )
                }
            }
        }
    }
}
