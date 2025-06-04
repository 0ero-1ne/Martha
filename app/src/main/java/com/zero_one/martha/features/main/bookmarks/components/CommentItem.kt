package com.zero_one.martha.features.main.bookmarks.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zero_one.martha.R
import com.zero_one.martha.data.domain.model.Comment
import io.dokar.expandabletext.ExpandableText

@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    comment: Comment,
    userId: UInt,
    userRole: String,
    onUpdateComment: () -> Unit,
    onDeleteComment: (UInt) -> Unit,
    onRateComment: (UInt, Boolean?) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(10.dp))
            .padding(10.dp),
    ) {
        val maxLines by remember {mutableIntStateOf(2)}
        var expanded by remember {mutableStateOf(false)}

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(comment.user.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Comment user image",
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.ic_no_cover),
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                            .clip(CircleShape),
                    )
                    Text(
                        text = comment.user.username,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                if (userId == comment.userId || userRole == "moderator" || userRole == "admin") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit comment button",
                            modifier = Modifier
                                .clickable {
                                    onUpdateComment()
                                }
                                .size(20.dp),
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete comment button",
                            tint = if (isSystemInDarkTheme())
                                Color(0xFFCD1C18)
                            else MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .clickable {
                                    onDeleteComment(comment.id)
                                }
                                .size(20.dp),
                        )
                    }
                }
            }
            ExpandableText(
                expanded = expanded,
                text = comment.text,
                collapsedMaxLines = maxLines,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                modifier = Modifier
                    .animateContentSize()
                    .clickable {expanded = !expanded}
                    .padding(bottom = 10.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    val commentRate =
                        comment.rates.firstOrNull {it.commentId == comment.id && it.userId == userId}

                    Icon(
                        imageVector = Icons.Outlined.ArrowUpward,
                        contentDescription = "Like comment button",
                        modifier = Modifier
                            .clickable {
                                if (userId != 0u) {
                                    if (commentRate == null) {
                                        onRateComment(comment.id, true)
                                    } else {
                                        if (commentRate.rating) {
                                            onRateComment(comment.id, null)
                                        } else {
                                            onRateComment(comment.id, true)
                                        }
                                    }
                                }
                            }
                            .size(20.dp)
                            .clip(CircleShape),
                        tint = if (comment.rates.any {it.rating && it.userId == userId}) {
                            if (isSystemInDarkTheme())
                                Color(0xFF006400)
                            else Color(0xFF00BB77)
                        } else MaterialTheme.colorScheme.primary,
                    )
                    val commentRateNumber =
                        comment.rates.count {it.rating} - comment.rates.count {!it.rating}
                    Text(
                        text = commentRateNumber.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (commentRateNumber < 0) {
                            if (isSystemInDarkTheme())
                                Color(0xFFCD1C18)
                            else MaterialTheme.colorScheme.error
                        } else if (commentRateNumber > 0) {
                            if (isSystemInDarkTheme())
                                Color(0xFF006400)
                            else Color(0xFF00BB77)
                        } else MaterialTheme.colorScheme.primary,
                    )
                    Icon(
                        imageVector = Icons.Outlined.ArrowDownward,
                        contentDescription = "Dislike comment button",
                        modifier = Modifier
                            .clickable {
                                if (userId != 0u) {
                                    if (commentRate == null) {
                                        onRateComment(comment.id, false)
                                    } else {
                                        if (!commentRate.rating) {
                                            onRateComment(comment.id, null)
                                        } else {
                                            onRateComment(comment.id, false)
                                        }
                                    }
                                }
                            }
                            .size(20.dp),
                        tint = if (comment.rates.any {!it.rating && it.userId == userId}) {
                            if (isSystemInDarkTheme())
                                Color(0xFFCD1C18)
                            else MaterialTheme.colorScheme.error
                        } else MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
