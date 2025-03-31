package com.zero_one.martha.features.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutClick: () -> Unit,
    onNavigateToHomePage: () -> Unit,
    onNavigateToLoginPage: () -> Unit
) {
    viewModel.isLogged()

    if (viewModel.user == null) {
        Column {
            Text("You are not authorized")
            Button(
                onClick = {
                    onNavigateToLoginPage()
                },
            ) {
                Text("Login")
            }
        }
    } else {
        Column {
            Text(viewModel.user!!.username)
            Button(
                onClick = {
                    onLogoutClick()
                    onNavigateToHomePage()
                },
            ) {
                Text("Logout")
            }
        }
    }
}
