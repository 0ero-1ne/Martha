package com.zero_one.martha.features.main.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zero_one.martha.R

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutClick: () -> Unit,
    onNavigateToHomePage: () -> Unit,
    onNavigateToLoginPage: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                )
                .fillMaxSize(),
        ) {
            viewModel.isLogged()

            if (viewModel.user == null || (viewModel.user != null && viewModel.user!!.id == 0u)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding() + 16.dp,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "You are not authorized",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                            ),
                        text = "Authorize to control your account",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    OutlinedButton(
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                            ),
                        onClick = onNavigateToLoginPage,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    ) {
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                return@Scaffold
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp,
                        bottom = 16.dp,
                    ),
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(viewModel.user!!.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = "User ${viewModel.user!!.id} image",
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.ic_no_cover),
                    modifier = Modifier
                        .height(200.dp)
                        .width(200.dp)
                        .clip(CircleShape),
                )
                IconButton(
                    onClick = {
                        onNavigateToHomePage()
                        onLogoutClick()
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Logout,
                        contentDescription = "Logout",
                    )
                }
            }
            Text(
                text = viewModel.user!!.username,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 16.dp,
                    ),
            )
            Text("User progress")
            viewModel.user!!.savedBooks.forEach {(key, value) ->
                Text(
                    text = "$key: ${value.size} books",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(bottom = 5.dp),
                )
            }
        }
    }
}
