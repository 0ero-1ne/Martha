package com.zero_one.martha.navigation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Bookmarks
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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.zero_one.martha.R
import com.zero_one.martha.features.main.bookmarks.BookmarksRoute
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
            stringResource(R.string.bookmarks_tab),
            BookmarksRoute(),
            Icons.Filled.Bookmarks,
            Icons.Outlined.Bookmarks,
        ),
        TopLevelRoute(
            stringResource(R.string.home_tab),
            HomeRoute,
            Icons.Filled.Home,
            Icons.Outlined.Home,
        ),
        TopLevelRoute(
            stringResource(R.string.catalog_tab),
            CatalogRoute,
            Icons.Filled.LocalLibrary,
            Icons.Outlined.LocalLibrary,
        ),
        TopLevelRoute(
            stringResource(R.string.profile_tab),
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
