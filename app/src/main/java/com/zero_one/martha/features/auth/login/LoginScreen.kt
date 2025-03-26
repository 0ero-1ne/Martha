package com.zero_one.martha.features.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zero_one.martha.ui.forms.login.LoginForm
import com.zero_one.martha.ui.forms.login.rememberLoginFormState

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginClick: (email: String, password: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar()
        },
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    bottom = paddingValues.calculateBottomPadding(),
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val formState = rememberLoginFormState()

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
            ) {
                LoginForm(state = formState)
                Button(
                    onClick = {
                        formState.validate()

                        if (formState.isValid) {
                            onLoginClick(formState.email.value, formState.password.value)
                        }
                    },
                ) {
                    Text("Login")
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar() {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = {},
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    "Navigate to back button",
                )
            }
        },
    )
}
