package com.zero_one.martha.features.auth.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zero_one.martha.features.auth.ui.AppBar
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
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        keyboardController?.hide()
                    },
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
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

            AppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 16.dp,
                    ),
                onNavigateToBack = onNavigateToBack,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )
                SignupForm(state = formState)
                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )
                Button(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        if (viewModel.isSignupButtonPressed) {
                            return@Button
                        }

                        keyboardController?.hide()
                        formState.validate()

                        if (formState.isValid) {
                            onSignupClick(formState.email.value, formState.password.value)
                        }
                    },
                ) {
                    if (viewModel.isSignupButtonPressed) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            text = "Signup",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            end = 5.dp,
                        ),
                    text = "Already has account?",
                )
                Text(
                    modifier = Modifier
                        .clickable {
                            onNavigateToBack()
                        },
                    text = "Login",
                    color = if (isSystemInDarkTheme())
                        Color(0xFF9874AA)
                    else
                        Color(0xFF251758),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    modifier = Modifier
                        .padding(
                            start = 5.dp,
                        ),
                    text = "now!",
                )
            }
        }
    }
}
