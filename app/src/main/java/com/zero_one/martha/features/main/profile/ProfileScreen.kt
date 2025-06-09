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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zero_one.martha.BuildConfig
import com.zero_one.martha.R
import com.zero_one.martha.features.main.profile.ui.BookmarkItem
import com.zero_one.martha.features.main.profile.ui.KarmaRating
import com.zero_one.martha.utils.parseSystemFolderName

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutClick: () -> Unit,
    onNavigateToHomePage: () -> Unit,
    onNavigateToLoginPage: () -> Unit,
    onNavigateToBookmarks: (folderName: String) -> Unit,
    onNavigateToEditPage: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) {paddingValues ->
        viewModel.isLogged()
        if (viewModel.user == null || (viewModel.user != null && viewModel.user!!.id == 0u)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.not_authorized),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                        ),
                    text = stringResource(R.string.authorize_message_profile),
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
                        text = stringResource(R.string.authorize_button),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                )
                .fillMaxWidth(),
        ) {

            val userComments = viewModel.userComments.collectAsState()
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                        .data("${BuildConfig.STORAGE_URL}images/${viewModel.user!!.image}")
                        .crossfade(true)
                        .build(),
                    contentDescription = "User ${viewModel.user!!.id} image",
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.ic_no_cover),
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(16.dp)),
                )
                Column {
                    Text(
                        text = viewModel.user!!.username,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = 16.dp,
                            ),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        OutlinedButton(
                            onClick = {
                                onNavigateToEditPage()
                            },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            modifier = Modifier.weight(1f),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit profile button",
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                onNavigateToHomePage()
                                onLogoutClick()
                            },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            modifier = Modifier.weight(1f),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Logout,
                                contentDescription = "Logout button",
                            )
                        }
                    }
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            var upvotes = 0
            var downvotes = 0
            userComments.value?.forEach {comment ->
                upvotes += comment.rates.filter {it.rating}.size
                downvotes += comment.rates.filter {!it.rating}.size
            }

            if (userComments.value != null) {
                KarmaRating(
                    upvotes = upvotes,
                    downvotes = downvotes,
                )
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            if (viewModel.user != null) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(viewModel.user!!.savedBooks.keys.toList(), key = {it}) {savedBook ->
                        BookmarkItem(
                            onNavigateToBookmarks = onNavigateToBookmarks,
                            savedBook = parseSystemFolderName(savedBook),
                            originalName = savedBook,
                            savedBookSize = viewModel.user!!.savedBooks[savedBook]?.size.toString(),
                        )
                    }
                }
            }

        }
    }
}
