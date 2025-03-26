package com.zero_one.martha.navigation.auth

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zero_one.martha.features.auth.login.LoginRoute
import com.zero_one.martha.features.auth.login.LoginScreen
import com.zero_one.martha.features.auth.login.LoginViewModel
import kotlinx.serialization.Serializable

@Serializable
object AuthNavigationGraph

fun NavGraphBuilder.authNavigationGraph(
    rootNavController: NavController
) {
    navigation<AuthNavigationGraph>(
        startDestination = LoginRoute,
    ) {
        composable<LoginRoute> {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                viewModel = viewModel,
                onLoginClick = viewModel::login,
            )
        }
    }
}
