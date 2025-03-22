package com.zero_one.martha.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zero_one.martha.navigation.auth.authNavigationGraph
import com.zero_one.martha.navigation.main.MainNavigationGraph
import com.zero_one.martha.navigation.main.MainNavigationHost

@Composable
fun RootNavigationHost() {
    val rootNavController = rememberNavController()
    NavHost(
        navController = rootNavController,
        startDestination = MainNavigationGraph,
    ) {
        composable<MainNavigationGraph> {
            MainNavigationHost(rootNavController)
        }
        authNavigationGraph(
            rootNavController = rootNavController,
        )
    }
}
