package com.zero_one.martha.features.player.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zero_one.martha.features.player.components.ChapterPlayerActions
import com.zero_one.martha.features.player.components.ChapterPlayerState
import com.zero_one.martha.ui.components.CustomIconButton

@Composable
fun PlayerControls(
    onAction: (ChapterPlayerActions) -> Unit,
    playerState: ChapterPlayerState,
    isLoading: Boolean
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
        CustomIconButton(
            onClick = {
                onAction(ChapterPlayerActions.Replay)
            },
            imageVector = Icons.Rounded.Replay10,
            size = 36.dp,
        )

        Button(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                )
                .wrapContentSize(),
            shape = CircleShape,
            enabled = !isLoading,
            onClick = {
                when (playerState) {
                    ChapterPlayerState.PLAYING -> onAction(ChapterPlayerActions.Pause)
                    ChapterPlayerState.ENDED, ChapterPlayerState.IDLE -> {
                        onAction(ChapterPlayerActions.SeekTo(0L))
                        onAction(ChapterPlayerActions.Play)
                    }

                    ChapterPlayerState.PAUSED -> onAction(
                        ChapterPlayerActions.Resume,
                    )
                }
            },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(82.dp))
            } else {
                Icon(
                    imageVector = if (playerState == ChapterPlayerState.PLAYING)
                        Icons.Rounded.Pause
                    else Icons.Rounded.PlayArrow,
                    contentDescription = "Play / Pause icons",
                    modifier = Modifier
                        .size(82.dp),
                )
            }
        }

        CustomIconButton(
            onClick = {
                onAction(ChapterPlayerActions.Forward)
            },
            imageVector = Icons.Rounded.Forward10,
            size = 36.dp,
        )
    }
}
