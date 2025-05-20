package com.zero_one.martha.features.player.components

import android.net.Uri

sealed interface ChapterPlayerActions {
    data class Init(val uri: Uri, val state: Long): ChapterPlayerActions
    data object Play: ChapterPlayerActions
    data object Release: ChapterPlayerActions
    data object Pause: ChapterPlayerActions
    data object Resume: ChapterPlayerActions
    data object Replay: ChapterPlayerActions
    data object Forward: ChapterPlayerActions
    data class SeekTo(val position: Long): ChapterPlayerActions
    data class SetPlaybackSpeed(val speed: Float): ChapterPlayerActions
}
