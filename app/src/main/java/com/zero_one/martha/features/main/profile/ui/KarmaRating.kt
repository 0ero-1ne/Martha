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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zero_one.martha.R

@Composable
fun KarmaRating(
    upvotes: Int,
    downvotes: Int
) {
    val context = LocalContext.current
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

    var karmaText = context.resources.getString(R.string.karma_no_activity)
    if (upvotes > downvotes) {
        karmaText = context.resources.getString(R.string.karma_great)
    } else if (downvotes > upvotes) {
        karmaText = context.resources.getString(R.string.karma_bad)
    } else if (upvotes != 0) {
        karmaText = context.resources.getString(R.string.karma_normal)
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
