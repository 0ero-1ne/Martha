package com.zero_one.martha.features.player.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zero_one.martha.features.player.components.ChapterPlayerActions
import com.zero_one.martha.features.player.components.ChapterPlayerState

@Composable
fun PlayerControls(
    onAction: (ChapterPlayerActions) -> Unit,
    playerState: ChapterPlayerState,
    isLoading: Boolean,
    uri: Uri
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                bottom = 10.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        IconButton(
            onClick = {
                onAction(ChapterPlayerActions.Replay)
            },
        ) {
            Icon(
                imageVector = Icons.Rounded.Replay10,
                contentDescription = "Replay 10 icon",
            )
        }
        IconButton(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                ),
            enabled = !isLoading,
            onClick = {
                when (playerState) {
                    ChapterPlayerState.PLAYING -> onAction(ChapterPlayerActions.Pause)
                    ChapterPlayerState.ENDED, ChapterPlayerState.IDLE -> onAction(
                        ChapterPlayerActions.Play(uri),
                    )

                    ChapterPlayerState.PAUSED -> onAction(
                        ChapterPlayerActions.Resume,
                    )
                }
            },
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Icon(
                    imageVector = if (playerState == ChapterPlayerState.PLAYING)
                        Icons.Rounded.Pause else
                        Icons.Rounded.PlayArrow,
                    contentDescription = "Play / Pause icons",
                )
            }
        }
        IconButton(
            onClick = {
                onAction(ChapterPlayerActions.Forward)
            },
        ) {
            Icon(
                imageVector = Icons.Rounded.Forward10,
                contentDescription = "Forward 10 icon",
            )
        }
    }
}
