package com.zero_one.martha.features.main.profile.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KarmaRating(
    upvotes: Int,
    downvotes: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        KarmaItem(
            votes = upvotes,
            imageVector = Icons.Default.ArrowUpward,
            color = if (isSystemInDarkTheme())
                Color(0xFF006400)
            else Color(0xFF00BB77),
        )
        KarmaItem(
            votes = downvotes,
            imageVector = Icons.Default.ArrowDownward,
            color = if (isSystemInDarkTheme())
                Color(0xFFCD1C18)
            else MaterialTheme.colorScheme.error,
        )
    }

    var karmaText = "No activity in comments"
    if (upvotes > downvotes) {
        karmaText = "You have a great karma"
    } else if (downvotes > upvotes) {
        karmaText = "You have a bad karma"
    } else if (upvotes != 0) {
        karmaText = "You have a good karma"
    }

    Text(
        text = karmaText,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    )
}

@Composable
private fun KarmaItem(
    modifier: Modifier = Modifier,
    votes: Int,
    imageVector: ImageVector,
    color: Color,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Karma item",
            tint = color,
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 5.dp),
        )
        Text(
            text = votes.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
    }
}
