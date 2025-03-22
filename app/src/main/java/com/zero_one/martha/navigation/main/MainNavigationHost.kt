package com.zero_one.martha.navigation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigationHost(
    rootNavController: NavController
) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier,
        bottomBar = {
            MainNavigationBar(
                navController = navController,
            )
        },
    ) {paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = MainNavigationGraph,
        ) {
            mainNavigationGraph(
                navController = navController,
                rootNavController = rootNavController,
            )
        }
    }
}
