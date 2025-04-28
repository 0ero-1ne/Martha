package com.zero_one.martha.features.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
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
        ) {
            if (viewModel.currentChapter == null) {
                CircularProgressIndicator()
                return@Scaffold
            }

            if (viewModel.currentChapter!!.id == 0U) {
                Text("Read chapter error")
                return@Scaffold
            }

            val playerState by chapterPlayerViewModel.playerState.collectAsState()
            val isLoading by chapterPlayerViewModel.isLoading.collectAsState()
            val sliderPosition by chapterPlayerViewModel.sliderPosition.collectAsState()

            AudioSlider(
                value = sliderPosition,
                onValueChange = {
                    chapterPlayerViewModel.onAction(ChapterPlayerActions.SeekTo(it.toLong()))
                },
                totalDuration = playerState.totalDuration,
            )

            PlayerControls(
                onAction = chapterPlayerViewModel::onAction,
                isLoading = isLoading,
                playerState = playerState.state,
                uri = viewModel.currentChapter!!.audio.toUri(),
            )
        }
    }
}
