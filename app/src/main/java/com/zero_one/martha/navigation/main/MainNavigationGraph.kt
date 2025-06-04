package com.zero_one.martha.navigation.main

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zero_one.martha.features.auth.login.LoginRoute
import com.zero_one.martha.features.main.book.BookRoute
import com.zero_one.martha.features.main.book.BookScreen
import com.zero_one.martha.features.main.book.BookViewModel
import com.zero_one.martha.features.main.book.UIntNavType
import com.zero_one.martha.features.main.bookmarks.BookmarksRoute
import com.zero_one.martha.features.main.bookmarks.BookmarksScreen
import com.zero_one.martha.features.main.bookmarks.BookmarksViewModel
import com.zero_one.martha.features.main.catalog.CatalogRoute
import com.zero_one.martha.features.main.catalog.CatalogScreen
import com.zero_one.martha.features.main.catalog.CatalogViewModel
import com.zero_one.martha.features.main.home.HomeRoute
import com.zero_one.martha.features.main.home.HomeScreen
import com.zero_one.martha.features.main.home.HomeViewModel
import com.zero_one.martha.features.main.profile.ProfileRoute
import com.zero_one.martha.features.main.profile.ProfileScreen
import com.zero_one.martha.features.main.profile.ProfileViewModel
import com.zero_one.martha.features.player.PlayerRoute
import com.zero_one.martha.features.reader.ReaderRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object MainNavigationGraph

fun NavGraphBuilder.mainNavigationGraph(
    navController: NavController,
    rootNavController: NavController
) {
    navigation<MainNavigationGraph>(
        startDestination = BookRoute(1u),
    ) {
        composable<HomeRoute> {
            HomeScreen(
                viewModel = hiltViewModel<HomeViewModel>(),
                onNavigateToBook = {bookId ->
                    navController.navigate(BookRoute(bookId = bookId))
                },
            )
        }
        composable<ProfileRoute> {
            val viewModel = hiltViewModel<ProfileViewModel>()

            ProfileScreen(
                viewModel = viewModel,
                onLogoutClick = viewModel::logout,
                onNavigateToHomePage = {
                    navController.navigate(HomeRoute)
                },
                onNavigateToLoginPage = {
                    rootNavController.navigate(LoginRoute)
                },
            )
        }
        composable<CatalogRoute> {
            val viewModel = hiltViewModel<CatalogViewModel>()

            CatalogScreen(
                viewModel = viewModel,
                onBookClick = {bookId ->
                    navController.navigate(BookRoute(bookId = bookId))
                },
            )
        }
        composable<BookmarksRoute> {
            val viewModel = hiltViewModel<BookmarksViewModel>()

            BookmarksScreen(
                viewModel = viewModel,
                onNavigateToLoginPage = {
                    rootNavController.navigate(LoginRoute)
                },
                onNavigateToBook = {bookId ->
                    navController.navigate(BookRoute(bookId))
                },
            )
        }
        composable<BookRoute>(
            typeMap = mapOf(
                typeOf<UInt>() to UIntNavType,
            ),
        ) {
            val viewModel = hiltViewModel<BookViewModel>()

            BookScreen(
                viewModel = viewModel,
                onNavigateToBack = {
                    navController.navigateUp()
                },
                onNavigateToReader = {bookId, chapterId ->
                    rootNavController.navigate(
                        ReaderRoute(
                            bookId = bookId,
                            chapterId = chapterId,
                        ),
                    )
                },
                onNavigateToPlayer = {bookId, chapterId ->
                    rootNavController.navigate(
                        PlayerRoute(
                            bookId = bookId,
                            chapterId = chapterId,
                        ),
                    )
                },
                onNavigateToLoginPage = {
                    rootNavController.navigate(LoginRoute)
                },
            )
        }
    }
}
