package com.zero_one.martha.navigation.auth

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zero_one.martha.features.auth.login.LoginRoute
import com.zero_one.martha.features.auth.login.LoginScreen
import com.zero_one.martha.features.auth.login.LoginViewModel
import com.zero_one.martha.features.auth.signup.SignupRoute
import com.zero_one.martha.features.auth.signup.SignupScreen
import com.zero_one.martha.features.auth.signup.SignupViewModel
import kotlinx.serialization.Serializable

@Serializable
object AuthNavigationGraph

fun NavGraphBuilder.authNavigationGraph(
    rootNavController: NavController
) {
    navigation<AuthNavigationGraph>(
        startDestination = LoginRoute,
    ) {
        composable<LoginRoute>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(500),
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(500),
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(500),
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(500),
                )
            },
        ) {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                viewModel = viewModel,
                onLoginClick = viewModel::login,
                onNavigateToBack = {
                    rootNavController.navigateUp()
                },
                onNavigateToSignupScreen = {
                    rootNavController.navigate(SignupRoute)
                },
            )
        }

        composable<SignupRoute>(
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(500),
                )
            },
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(500),
                )
            },
        ) {
            val viewModel = hiltViewModel<SignupViewModel>()
            SignupScreen(
                viewModel = viewModel,
                onSignupClick = viewModel::signup,
                onNavigateToBack = {
                    rootNavController.navigateUp()
                },
            )
        }
    }
}
