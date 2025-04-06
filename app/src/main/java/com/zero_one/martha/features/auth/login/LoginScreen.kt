package com.zero_one.martha.features.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zero_one.martha.ui.components.CustomTopBar
import com.zero_one.martha.ui.forms.login.LoginForm
import com.zero_one.martha.ui.forms.login.rememberLoginFormState
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginClick: (email: String, password: String) -> Unit,
    onNavigateToBack: () -> Unit,
    onNavigateToSignupScreen: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {SnackbarHostState()}
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember {MutableInteractionSource()}

    Scaffold(
        topBar = {
            CustomTopBar(
                onNavigateToBack = onNavigateToBack,
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
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
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        keyboardController?.hide()
                    },
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val formState = rememberLoginFormState()
            val context = LocalContext.current

            LaunchedEffect(key1 = context) {
                viewModel.loginValidationEvents.collect {event ->
                    when (event) {
                        is LoginViewModel.LoginValidationEvent.Success -> {
                            onNavigateToBack()
                        }

                        LoginViewModel.LoginValidationEvent.Error -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(viewModel.loginErrorMessage)
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
            ) {
                if (viewModel.isLoginButtonPressed) {
                    CircularProgressIndicator()
                }
                LoginForm(state = formState)
                Button(
                    onClick = {
                        keyboardController?.hide()
                        formState.validate()

                        if (formState.isValid) {
                            onLoginClick(formState.email.value, formState.password.value)
                        }
                    },
                ) {
                    Text("Login")
                }
                Button(
                    onClick = onNavigateToSignupScreen,
                ) {
                    Text("Signup form")
                }
            }
        }
    }
}
