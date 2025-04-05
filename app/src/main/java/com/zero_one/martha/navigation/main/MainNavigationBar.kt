package com.zero_one.martha.navigation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.zero_one.martha.features.main.catalog.CatalogRoute
import com.zero_one.martha.features.main.home.HomeRoute
import com.zero_one.martha.features.main.profile.ProfileRoute

data class TopLevelRoute<T: Any>(
    val name: String,
    val route: T,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
)

@Composable
fun MainNavigationBar(
    navController: NavHostController
) {
    val topLevelRoutes = listOf(
        TopLevelRoute(
            "Home",
            HomeRoute,
            Icons.Filled.Home,
            Icons.Outlined.Home,
        ),
        TopLevelRoute(
            "Catalog",
            CatalogRoute,
            Icons.Filled.LocalLibrary,
            Icons.Outlined.LocalLibrary,
        ),
        TopLevelRoute(
            "Me",
            ProfileRoute,
            Icons.Filled.Person,
            Icons.Outlined.Person,
        ),
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        topLevelRoutes.forEach {route ->
            NavigationBarItem(
                icon = {
                    if (currentDestination?.hierarchy?.any {
                            it.hasRoute(route.route::class)
                        } == true)
                        Icon(route.selectedIcon, route.name)
                    else
                        Icon(route.unSelectedIcon, route.name)
                },
                label = {Text(route.name)},
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(route.route::class)
                } == true,
                onClick = {
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}
