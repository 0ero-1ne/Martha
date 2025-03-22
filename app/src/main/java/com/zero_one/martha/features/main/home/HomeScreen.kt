package com.zero_one.martha.features.main.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToLoginPage: () -> Unit
) {
    Button(
        onClick = onNavigateToLoginPage,
    ) {
        Text("Login")
    }
}
