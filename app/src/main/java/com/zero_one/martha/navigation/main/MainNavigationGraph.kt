package com.zero_one.martha.navigation.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zero_one.martha.features.home.HomeRoute
import com.zero_one.martha.features.home.HomeScreen
import com.zero_one.martha.features.profile.ProfileRoute
import com.zero_one.martha.features.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
object MainNavigationGraph

fun NavGraphBuilder.mainNavigationGraph() {
    navigation<MainNavigationGraph>(
        startDestination = HomeRoute,
    ) {
        composable<HomeRoute> {
            HomeScreen()
        }
        composable<ProfileRoute> {
            ProfileScreen()
        }
    }
}
