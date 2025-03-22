package com.zero_one.martha.navigation.main

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zero_one.martha.features.auth.login.LoginRoute
import com.zero_one.martha.features.main.home.HomeRoute
import com.zero_one.martha.features.main.home.HomeScreen
import com.zero_one.martha.features.main.home.HomeViewModel
import com.zero_one.martha.features.main.profile.ProfileRoute
import com.zero_one.martha.features.main.profile.ProfileScreen
import kotlinx.serialization.Serializable

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
                onNavigateToLoginPage = {
                    rootNavController.navigate(LoginRoute)
                },
            )
        }
        composable<ProfileRoute> {
            ProfileScreen()
        }
    }
}
