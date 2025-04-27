package com.zero_one.martha.features.player.components

data class PlayerState(
    val state: ChapterPlayerState = ChapterPlayerState.IDLE,
    val totalDuration: Long = 0
)

enum class ChapterPlayerState {
    IDLE, PLAYING, PAUSED, ENDED
}
