package com.zero_one.martha.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    imageVector: ImageVector,
    size: Dp,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Button(
        enabled = enabled,
        modifier = modifier
            .size(size + 10.dp)
            .padding(paddingValues),
        onClick = onClick,
        content = {
            Icon(
                modifier = Modifier.size(size),
                imageVector = imageVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        contentPadding = PaddingValues(2.dp),
    )
}
