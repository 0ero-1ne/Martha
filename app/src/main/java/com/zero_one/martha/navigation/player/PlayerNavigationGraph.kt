package com.zero_one.martha.navigation.player

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zero_one.martha.features.main.book.UIntNavType
import com.zero_one.martha.features.player.PlayerRoute
import com.zero_one.martha.features.player.PlayerScreen
import com.zero_one.martha.features.player.PlayerViewModel
import com.zero_one.martha.features.player.components.ChapterPlayerViewModel
import com.zero_one.martha.navigation.reader.FakeRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object PlayerNavigationGraph

fun NavGraphBuilder.playerNavigationGraph(
    rootNavController: NavController
) {
    navigation<PlayerNavigationGraph>(
        startDestination = FakeRoute,
    ) {
        composable<FakeRoute> {}
        composable<PlayerRoute>(
            typeMap = mapOf(
                typeOf<UInt>() to UIntNavType,
            ),
        ) {
            val viewModel = hiltViewModel<PlayerViewModel>()
            val chapterPlayerViewModel = hiltViewModel<ChapterPlayerViewModel>()
            PlayerScreen(
                viewModel = viewModel,
                chapterPlayerViewModel = chapterPlayerViewModel,
                onNavigateToBack = {
                    rootNavController.navigateUp()
                },
            )
        }
    }
}
