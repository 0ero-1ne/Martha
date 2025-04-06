package com.zero_one.martha.navigation.main

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zero_one.martha.features.auth.login.LoginRoute
import com.zero_one.martha.features.main.book.BookIdNavType
import com.zero_one.martha.features.main.book.BookRoute
import com.zero_one.martha.features.main.book.BookScreen
import com.zero_one.martha.features.main.book.BookViewModel
import com.zero_one.martha.features.main.catalog.CatalogRoute
import com.zero_one.martha.features.main.catalog.CatalogScreen
import com.zero_one.martha.features.main.catalog.CatalogViewModel
import com.zero_one.martha.features.main.home.HomeRoute
import com.zero_one.martha.features.main.home.HomeScreen
import com.zero_one.martha.features.main.home.HomeViewModel
import com.zero_one.martha.features.main.profile.ProfileRoute
import com.zero_one.martha.features.main.profile.ProfileScreen
import com.zero_one.martha.features.main.profile.ProfileViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object MainNavigationGraph

fun NavGraphBuilder.mainNavigationGraph(
    navController: NavController,
    rootNavController: NavController
) {
    navigation<MainNavigationGraph>(
        startDestination = HomeRoute,
    ) {
        composable<HomeRoute> {
            HomeScreen(
                viewModel = viewModel<HomeViewModel>(),
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
        composable<BookRoute>(
            typeMap = mapOf(
                typeOf<UInt>() to BookIdNavType,
            ),
        ) {
            val viewModel = hiltViewModel<BookViewModel>()

            BookScreen(
                viewModel = viewModel,
                onNavigateToBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
