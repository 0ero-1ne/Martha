package com.zero_one.martha.features.main.catalog.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zero_one.martha.R
import com.zero_one.martha.data.domain.model.Tag
import com.zero_one.martha.utils.parseBookStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersMenu(
    tags: List<Tag>,
    yearsLimits: Pair<Int, Int>,
    yearCurrentValue: Pair<Int, Int>,
    onDismiss: () -> Unit,
    onAddTagFilter: (String) -> Unit,
    onRemoveTagFilter: (String) -> Unit,
    onAddStatusFilter: (String) -> Unit,
    onRemoveStatusFilter: (String) -> Unit,
    onSetYearFilter: (start: Int, end: Int) -> Unit,
    tagFilters: State<List<String>>,
    statusFilters: State<List<String>>,
    onApplyFilters: () -> Unit,
    state: SheetState,
) {
    val interactionSource = remember {MutableInteractionSource()}

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = state,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                ),
        ) {
            Text(
                text = stringResource(R.string.filters_tags_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 10.dp),
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                tags.forEach {tag ->
                    val isCheckedState by remember {
                        derivedStateOf {
                            tagFilters.value.contains(tag.title)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                            ) {
                                if (!isCheckedState) {
                                    onAddTagFilter(tag.title)
                                } else {
                                    onRemoveTagFilter(tag.title)
                                }
                            }
                            .border(
                                width = if (!isCheckedState) 1.dp else 2.dp,
                                color = if (!isCheckedState)
                                    MaterialTheme.colorScheme.outline
                                else MaterialTheme.colorScheme.primary,
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
                text = stringResource(R.string.filters_year_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 5.dp),
            )

            YearPicker(
                minValue = yearsLimits.first,
                maxValue = yearsLimits.second,
                yearCurrentValue = yearCurrentValue,
                onValueChangeFinished = {start, end ->
                    onSetYearFilter(start, end)
                },
            )

            Text(
                text = stringResource(R.string.about_short_status),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 5.dp, top = 16.dp),
            )

            val statuses = listOf("Ended", "Ongoing", "Stopped")
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                statuses.forEach {status ->
                    val isCheckedState by remember {
                        derivedStateOf {
                            statusFilters.value.contains(status)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                            ) {
                                if (!isCheckedState) {
                                    onAddStatusFilter(status)
                                } else {
                                    onRemoveStatusFilter(status)
                                }
                            }
                            .border(
                                width = if (!isCheckedState) 1.dp else 2.dp,
                                color = if (!isCheckedState)
                                    MaterialTheme.colorScheme.outline
                                else MaterialTheme.colorScheme.primary,
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
                            text = parseBookStatus(status),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(56.dp),
                onClick = {
                    onDismiss()
                    onApplyFilters()
                },
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.filters_use_title),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}
