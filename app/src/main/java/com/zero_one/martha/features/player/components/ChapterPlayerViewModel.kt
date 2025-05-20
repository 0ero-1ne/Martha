package com.zero_one.martha.features.player.components

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterPlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
): ViewModel() {
    private var chapterPlayer: ExoPlayer? = null
    private var listener: Player.Listener? = null

    var isInit by mutableStateOf(false)

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _sliderPosition = MutableStateFlow(0L)
    val sliderPosition = _sliderPosition.asStateFlow()

    init {
        viewModelScope.launch {
            while (isActive) {
                updateSliderPosition(
                    chapterPlayer
                        ?.currentPosition
                        ?.coerceAtLeast(0L) ?: 0L,
                )
                delay(100L)
            }
        }
    }

    fun onAction(action: ChapterPlayerActions) {
        when (action) {
            is ChapterPlayerActions.Init -> init(action.uri, action.state)
            is ChapterPlayerActions.Play -> resume()
            is ChapterPlayerActions.Pause -> pause()
            is ChapterPlayerActions.Resume -> resume()
            is ChapterPlayerActions.SeekTo -> seekTo(action.position)
            is ChapterPlayerActions.Replay -> replay()
            is ChapterPlayerActions.Forward -> forward()
            is ChapterPlayerActions.Release -> release()
            is ChapterPlayerActions.SetPlaybackSpeed -> setPlaybackSpeed(action.speed)
        }
    }

    private fun init(uri: Uri, state: Long) {
        updateIsLoading(true)

        listener = object: Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                _playerState.value = PlayerState(
                    state = player.playbackState.toPlayerState(player.isPlaying),
                    totalDuration = player.duration.coerceAtLeast(0L),
                )
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (isLoading.value && (playbackState == Player.STATE_READY || playbackState == Player.STATE_IDLE)) {
                    updateIsLoading(false)
                }
            }
        }

        if (chapterPlayer?.isPlaying == true) {
            chapterPlayer?.stop()
        }

        chapterPlayer = ExoPlayer.Builder(context)
            .build()
            .apply {
                listener?.let {addListener(it)}
                addAnalyticsListener(EventLogger())
                addMediaItem(MediaItem.fromUri(uri))
                setPlaybackSpeed(1F)
                prepare()
                isInit = true
                seekTo(state)
                updateSliderPosition(state)
            }
    }

    private fun resume() {
        chapterPlayer?.play()
    }

    private fun pause() {
        chapterPlayer?.pause()
    }

    private fun replay() {
        chapterPlayer?.seekTo(
            (sliderPosition.value - 10000L).coerceAtLeast(0),
        )
    }

    private fun forward() {
        chapterPlayer?.seekTo(
            (sliderPosition.value + 10000L)
                .coerceAtMost(playerState.value.totalDuration),
        )
    }

    private fun seekTo(position: Long) {
        chapterPlayer?.seekTo(position)
        updateSliderPosition(position)
    }

    private fun setPlaybackSpeed(speed: Float) {
        chapterPlayer?.setPlaybackSpeed(speed)
    }

    private fun release() {
        chapterPlayer?.pause()
        chapterPlayer?.apply {
            listener?.let {removeListener(it)}
            this.release()
        }
        listener = null
        chapterPlayer = null
    }

    private fun updateIsLoading(loading: Boolean) {
        _isLoading.update {loading}
    }

    private fun updateSliderPosition(position: Long) {
        _sliderPosition.update {position}
    }

    private fun Int.toPlayerState(isPlaying: Boolean): ChapterPlayerState = when (this) {
        Player.STATE_IDLE -> ChapterPlayerState.IDLE
        Player.STATE_ENDED -> ChapterPlayerState.ENDED
        else -> if (isPlaying) ChapterPlayerState.PLAYING else ChapterPlayerState.PAUSED
    }
}
