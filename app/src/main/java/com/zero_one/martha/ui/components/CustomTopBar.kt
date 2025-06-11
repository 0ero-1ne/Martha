package com.zero_one.martha.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomTopBar(
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    onNavigateToBack: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier
            .padding(top = 0.dp),
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onNavigateToBack,
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    "Navigate to back button",
                )
            }
        },
        windowInsets = windowInsets,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomTopBarWithDropdownMenu(
    toSurfaceColor: Boolean = false,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    onNavigateToBack: () -> Unit,
    title: String = "",
    content: @Composable () -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .padding(top = 0.dp),
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateToBack,
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    "Navigate to back button",
                )
            }
        },
        actions = {
            content()
        },
        windowInsets = windowInsets,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (toSurfaceColor) MaterialTheme.colorScheme.surfaceContainer
            else MaterialTheme.colorScheme.background,
        ),
    )
}
