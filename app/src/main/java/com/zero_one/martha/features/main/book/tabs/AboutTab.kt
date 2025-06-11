package com.zero_one.martha.features.main.book.tabs

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zero_one.martha.R
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.features.main.book.ui.AboutShortInfo
import io.dokar.expandabletext.ExpandableText

@Composable
fun AboutTab(
    book: Book
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        var expandableDescription by remember {mutableStateOf(false)}

        AboutShortInfo(
            book = book,
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                ),
        )

        Text(
            text = stringResource(R.string.about_tags_title),
            style = MaterialTheme.typography.titleLarge,
        )
        FlowRow(
            modifier = Modifier
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            book.tags.forEach {tag ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(10.dp),
                        ),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(
                                top = 6.dp,
                                bottom = 6.dp,
                                start = 12.dp,
                                end = 12.dp,
                            ),
                        text = tag.title,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        Text(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                    bottom = 6.dp,
                ),
            text = stringResource(R.string.about_description_title),
            style = MaterialTheme.typography.titleLarge,
        )
        ExpandableText(
            expanded = expandableDescription,
            text = book.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            collapsedMaxLines = 7,
            modifier = Modifier
                .animateContentSize()
                .clickable {expandableDescription = !expandableDescription},
        )
    }
}
