package com.tondracek.myfarmer.ui.common.category

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

data class CategoryNameInputState(
    val categoryName: String,
    val suggestions: List<CategoryPopularity>,
) {
    companion object {
        val Initial = CategoryNameInputState(
            categoryName = "",
            suggestions = emptyList(),
        )
    }
}

@Composable
fun CategoryNameInput(
    modifier: Modifier = Modifier,
    state: CategoryNameInputState,
    onNameChanged: (String) -> Unit,
    onSuggestionClicked: (String) -> Unit = onNameChanged,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.categoryName,
            onValueChange = { onNameChanged(it) },
            label = { Text("Name") },
            singleLine = true,
        )
        Row(
            modifier = Modifier.horizontalScroll(state = rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Suggestions:")
            state.suggestions.forEach { suggestion ->
                SuggestionChip(
                    name = suggestion.name,
                    onClick = { onSuggestionClicked(suggestion.name) }
                )
            }
        }
    }
}

@Composable
private fun SuggestionChip(
    name: String,
    onClick: () -> Unit
) {
    Card(
        colors = MyFarmerTheme.cardColors.primary,
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.padding(8.dp, 0.dp),
            text = name
        )
    }
}
