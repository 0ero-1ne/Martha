package com.zero_one.martha.features.main.catalog.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zero_one.martha.data.domain.model.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersMenu(
    tags: List<Tag>,
    onDismiss: () -> Unit,
    onAddTagFilter: (String) -> Unit,
    onRemoveTagFilter: (String) -> Unit,
    tagFilters: State<List<String>>,
    onApplyFilters: () -> Unit,
    state: SheetState,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = state,
    ) {
        Column {
            Text("Tags")
            tags.forEach {tag ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val isCheckedState by remember {
                        derivedStateOf {
                            tagFilters.value.contains(tag.title)
                        }
                    }

                    Text(tag.title)
                    Checkbox(
                        checked = isCheckedState,
                        onCheckedChange = {
                            if (it) {
                                Log.d("Tag filter", "Add ${tag.title}")
                                onAddTagFilter(tag.title)
                            } else {
                                Log.d("Tag filter", "Remove ${tag.title}")
                                onRemoveTagFilter(tag.title)
                            }
                        },
                    )
                }
            }
            Button(
                onClick = {
                    onDismiss()
                    onApplyFilters()
                },
            ) {
                Text("Use filters")
            }
        }
    }
}
