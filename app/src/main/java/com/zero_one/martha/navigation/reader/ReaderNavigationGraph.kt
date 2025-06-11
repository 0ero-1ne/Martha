package com.zero_one.martha.navigation.reader

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zero_one.martha.features.main.book.UIntNavType
import com.zero_one.martha.features.reader.ReaderRoute
import com.zero_one.martha.features.reader.ReaderScreen
import com.zero_one.martha.features.reader.ReaderViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object ReaderNavigationGraph

@Serializable
object FakeRoute

fun NavGraphBuilder.readerNavigationGraph(
    rootNavController: NavController
) {
    navigation<ReaderNavigationGraph>(
        startDestination = FakeRoute,
    ) {
        composable<FakeRoute> {}
        composable<ReaderRoute>(
            typeMap = mapOf(
                typeOf<UInt>() to UIntNavType,
            ),
        ) {
            val viewModel = hiltViewModel<ReaderViewModel>()
            ReaderScreen(
                viewModel = viewModel,
                onNavigateToBack = {
                    rootNavController.navigateUp()
                },
                onNavigateToReader = {bookId, chapterId ->
                    rootNavController.navigate(
                        ReaderRoute(
                            bookId = bookId,
                            chapterId = chapterId,
                        ),
                    )
                },
            )
        }
    }
}
