package com.zero_one.martha.features.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zero_one.martha.BuildConfig
import com.zero_one.martha.R
import com.zero_one.martha.features.auth.ui.AppBar
import com.zero_one.martha.features.player.components.ChapterPlayerActions
import com.zero_one.martha.features.player.components.ChapterPlayerViewModel
import com.zero_one.martha.features.player.ui.AudioSlider
import com.zero_one.martha.features.player.ui.PlayerControls

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    chapterPlayerViewModel: ChapterPlayerViewModel,
    onNavigateToBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val playerState by chapterPlayerViewModel.playerState.collectAsState()
            val isLoading by chapterPlayerViewModel.isLoading.collectAsState()
            val sliderPosition by chapterPlayerViewModel.sliderPosition.collectAsState()

            BackHandler(
                onBack = {
                    chapterPlayerViewModel.onAction(ChapterPlayerActions.Pause)
                    viewModel.destroy(sliderPosition)
                    onNavigateToBack()
                },
            )

            AppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 32.dp,
                        top = 16.dp,
                    ),
                onNavigateToBack = {
                    chapterPlayerViewModel.onAction(ChapterPlayerActions.Pause)
                    viewModel.destroy(sliderPosition)
                    onNavigateToBack()
                },
            )


            if (viewModel.currentChapter == null || viewModel.timeState == -1L) {
                CircularProgressIndicator()
                return@Scaffold
            }

            if (viewModel.currentChapter!!.id == 0U) {
                Text("Sorry, but book still has ho any chapters")
                return@Scaffold
            }

            Column(
                modifier = Modifier
                    .height(400.dp)
                    .width(300.dp)
                    .padding(bottom = 32.dp)
                    .shadow(
                        elevation = 8.dp,
                        clip = true,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = MaterialTheme.colorScheme.primary,
                        spotColor = MaterialTheme.colorScheme.primary,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(viewModel.book!!.cover)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Book ${viewModel.book!!.id} cover",
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.ic_no_cover),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                )
            }

            Text(
                textAlign = TextAlign.Center,
                text = viewModel.currentChapter!!.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth(0.85f),
            )

            Text(
                textAlign = TextAlign.Center,
                text = viewModel.book!!.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.85f),
            )

            Spacer(Modifier.weight(2f))

            AudioSlider(
                value = sliderPosition,
                onValueChange = {
                    chapterPlayerViewModel.onAction(ChapterPlayerActions.SeekTo(it.toLong()))
                },
                totalDuration = playerState.totalDuration,
                modifier = Modifier,
            )

            val chapterUri = "${BuildConfig.STORAGE_URL}audios/${viewModel.currentChapter!!.audio}"
            if (!chapterPlayerViewModel.isInit) {
                chapterPlayerViewModel.onAction(
                    ChapterPlayerActions.Init(
                        chapterUri.toUri(),
                        viewModel.timeState,
                    ),
                )
            }

            PlayerControls(
                onAction = chapterPlayerViewModel::onAction,
                isLoading = isLoading,
                playerState = playerState.state,
            )

            Spacer(Modifier.weight(1f))
        }
    }
}
