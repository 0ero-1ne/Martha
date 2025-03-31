package com.zero_one.martha.features.auth.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.zero_one.martha.ui.forms.signup.SignupForm
import com.zero_one.martha.ui.forms.signup.rememberSignupFormState
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(
    viewModel: SignupViewModel,
    onNavigateToBack: () -> Unit,
    onSignupClick: (email: String, password: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {SnackbarHostState()}
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember {MutableInteractionSource()}

    Scaffold(
        topBar = {
            TopBar(
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
            val formState = rememberSignupFormState()
            val context = LocalContext.current

            LaunchedEffect(key1 = context) {
                viewModel.signupValidationEvents.collect {event ->
                    when (event) {
                        is SignupViewModel.SignupValidationEvent.Success -> {
                            onNavigateToBack()
                            onNavigateToBack()
                        }

                        SignupViewModel.SignupValidationEvent.Error -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(viewModel.signupErrorMessage)
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
            ) {
                if (viewModel.isSignupButtonPressed) {
                    CircularProgressIndicator()
                }
                SignupForm(state = formState)
                Button(
                    onClick = {
                        keyboardController?.hide()
                        formState.validate()

                        if (formState.isValid) {
                            onSignupClick(formState.email.value, formState.password.value)
                        }
                    },
                ) {
                    Text("Signup")
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    onNavigateToBack: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onNavigateToBack,
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    "Navigate to back button",
                )
            }
        },
    )
}
