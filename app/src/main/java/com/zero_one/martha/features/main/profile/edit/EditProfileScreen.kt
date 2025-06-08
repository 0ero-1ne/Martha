package com.zero_one.martha.features.main.profile.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zero_one.martha.BuildConfig
import com.zero_one.martha.R
import com.zero_one.martha.features.main.profile.edit.ui.GeneralForm
import com.zero_one.martha.features.main.profile.edit.ui.PasswordForm
import com.zero_one.martha.ui.components.CustomTabRow
import com.zero_one.martha.ui.components.CustomTopBar
import com.zero_one.martha.ui.forms.changePassword.rememberChangePasswordFormState
import com.zero_one.martha.ui.forms.edit.rememberEditFormState
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun EditProfileScreen(
    onNavigateToBack: () -> Unit,
    viewModel: EditProfileViewModel
) {
    val snackbarHostState = remember {SnackbarHostState()}

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomTopBar(
                onNavigateToBack = onNavigateToBack,
                windowInsets = WindowInsets(0),
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) {paddingValues ->
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var fileUri by remember {mutableStateOf<Uri?>(null)}
        val editFormState = rememberEditFormState()
        val changePasswordFormState = rememberChangePasswordFormState()

        LaunchedEffect(key1 = context) {
            viewModel.editValidationEvents.collect {event ->
                when (event) {
                    EditProfileViewModel.EditValidationEvent.EditSuccess -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("User was successfully updated")
                            fileUri = null
                        }
                    }

                    EditProfileViewModel.EditValidationEvent.PasswordSuccess -> {
                        changePasswordFormState.oldPassword.onValueChanged("")
                        changePasswordFormState.newPassword.onValueChanged("")
                        scope.launch {
                            snackbarHostState.showSnackbar("Password was successfully updated")
                            fileUri = null
                        }
                    }

                    EditProfileViewModel.EditValidationEvent.ImageSuccess -> {
                        fileUri = null
                        scope.launch {
                            snackbarHostState.showSnackbar("Image was successfully updated")
                        }
                    }

                    EditProfileViewModel.EditValidationEvent.EditError -> {
                        if (viewModel.editErrorMessage.contains("Email")) {
                            editFormState.email.error = viewModel.editErrorMessage
                            return@collect
                        }

                        if (viewModel.editErrorMessage.contains("Username")) {
                            editFormState.username.error = viewModel.editErrorMessage
                            return@collect
                        }

                        scope.launch {
                            snackbarHostState.showSnackbar(viewModel.editErrorMessage)
                        }
                    }

                    EditProfileViewModel.EditValidationEvent.PasswordError -> {
                        if (viewModel.editErrorMessage.contains("old password")) {
                            changePasswordFormState.oldPassword.error = viewModel.editErrorMessage
                            return@collect
                        }

                        scope.launch {
                            snackbarHostState.showSnackbar(viewModel.editErrorMessage)
                        }
                    }

                    EditProfileViewModel.EditValidationEvent.ImageError -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(viewModel.editErrorMessage)
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    top = paddingValues.calculateTopPadding() + 16.dp,
                ),
        ) {
            val user = viewModel.user.collectAsState()

            if (editFormState.email.value.isEmpty() && editFormState.username.value.isEmpty()) {
                editFormState.email.onValueChanged(user.value.email)
                editFormState.username.onValueChanged(user.value.username)
            }

            val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = {
                    fileUri = it
                },
            )

            if (user.value.id == 0u) {
                return@Scaffold
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box {
                    if (fileUri != null) {
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape)
                                .zIndex(2f)
                                .align(Alignment.TopEnd)
                                .clickable {
                                    fileUri = null
                                },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove chosen image",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(3.dp),
                            )
                        }
                    }
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(
                                fileUri ?: "${BuildConfig.STORAGE_URL}images/${user.value.image}",
                            )
                            .crossfade(true)
                            .build(),
                        contentDescription = "User ${user.value.id} image",
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.ic_no_cover),
                        modifier = Modifier
                            .height(150.dp)
                            .width(150.dp)
                            .clip(RoundedCornerShape(16.dp)),
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    OutlinedButton(
                        enabled = !viewModel.isLoading,
                        onClick = {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(
                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
                                ),
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.ImageSearch,
                            contentDescription = "Choose image button",
                            modifier = Modifier
                                .padding(end = 5.dp),
                        )
                        Text("Choose image")
                    }
                    OutlinedButton(
                        enabled = !viewModel.isLoading && fileUri != null,
                        onClick = {
                            val file = File(context.cacheDir, "file.jpg")
                            file.createNewFile()
                            file.outputStream().use {
                                val inputStream = context.contentResolver.openInputStream(fileUri!!)
                                inputStream?.copyTo(it)
                                inputStream?.close()
                            }
                            viewModel.uploadImage(file)
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator()
                            return@OutlinedButton
                        }
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save image button",
                            modifier = Modifier
                                .padding(end = 5.dp),
                        )
                        Text("Save image")
                    }
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 24.dp))

            val pagerState = rememberPagerState(
                pageCount = {
                    2
                },
            )
            val tabs = listOf("General", "Password")

            CustomTabRow(
                pagerState = pagerState,
                tabs = tabs.toSet(),
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(16.dp),
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(16.dp),
                    ),
            )

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize(),
                state = pagerState,
                pageSpacing = 16.dp,
                verticalAlignment = Alignment.Top,
            ) {page ->
                Column {
                    when (page) {
                        0 -> GeneralForm(
                            editFormState = editFormState,
                            isLoading = viewModel.isLoading,
                            onSave = viewModel::onSave,
                        )

                        1 -> PasswordForm(
                            changePasswordFormState = changePasswordFormState,
                            isLoading = viewModel.isLoading,
                            onChangePassword = viewModel::onChangePassword,
                        )
                    }
                }
            }
        }
    }
}
