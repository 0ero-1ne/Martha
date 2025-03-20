package com.zero_one.martha.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zero_one.martha.navigation.main.MainNavigationGraph
import com.zero_one.martha.navigation.main.MainNavigationHost

@Composable
fun RootNavigationHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainNavigationGraph,
    ) {
        composable<MainNavigationGraph> {
            MainNavigationHost()
        }
    }
}
