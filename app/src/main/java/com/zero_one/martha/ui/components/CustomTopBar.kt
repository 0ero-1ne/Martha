package com.zero_one.martha.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomTopBar(
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    onNavigateToBack: () -> Unit
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
