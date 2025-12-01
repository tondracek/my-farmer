package com.tondracek.myfarmer.ui.createshopflow.components.addcategorydialog

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun AddCategoryDialog(
    state: AddCategoryDialogState,
    onCategoryNameChange: (String) -> Unit,
    onColorSelected: (Color) -> Unit,
    onAdd: (ShopCategory) -> Unit,
    onDismiss: () -> Unit
) {
    val availableColors = listOf(
        Color(0xFFE57373),
        Color(0xFFF06292),
        Color(0xFFBA68C8),
        Color(0xFF9575CD),
        Color(0xFF7986CB),
        Color(0xFF64B5F6),
        Color(0xFF4FC3F7),
        Color(0xFF4DD0E1),
        Color(0xFF4DB6AC),
        Color(0xFF81C784),
        Color(0xFFAED581),
        Color(0xFFDCE775),
        Color(0xFFFFF176),
        Color(0xFFFFD54F),
        Color(0xFFFFB74D),
        Color(0xFFA1887F)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MyFarmerTheme.paddings.medium),
                colors = MyFarmerTheme.cardColors.custom(state.selectedColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Add category",
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CategoryNameInput(
                    state = state,
                    onNameChanged = onCategoryNameChange,
                    onSuggestionClicked = { onCategoryNameChange(it) }
                )
                ColorPicker(
                    availableColors = availableColors,
                    onColorSelected = onColorSelected
                )
            }
        },
        confirmButton = {
            TextButton(
                colors = MyFarmerTheme.buttonColors.custom(state.selectedColor),
                onClick = {
                    onAdd(
                        ShopCategory(
                            name = state.categoryName,
                            color = state.selectedColor
                        )
                    )
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryNameInput(
    state: AddCategoryDialogState,
    onNameChanged: (String) -> Unit,
    onSuggestionClicked: (String) -> Unit
) {
    Column {
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

@Composable
private fun ColorPicker(
    availableColors: List<Color>,
    onColorSelected: (Color) -> Unit
) {
    val density = LocalDensity.current

    val colorsInRow = 8
    val colorsPerRow: List<List<Color>> = availableColors.chunked(colorsInRow)

    Card(colors = MyFarmerTheme.cardColors.base) {
        Column {
            colorsPerRow.forEach { colorRow ->
                var colorOptionSize by remember { mutableStateOf(24.dp) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            val availableWidth = with(density) { it.size.width.toDp() }
                            colorOptionSize = (availableWidth / colorsInRow)
                        },
                ) {
                    colorRow.forEach { color ->
                        ColorOption(
                            size = colorOptionSize,
                            color = color,
                            onClick = { onColorSelected(color) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorOption(
    size: Dp,
    color: Color,
    onClick: () -> Unit
) {
    val padding = 4.dp

    Surface(
        modifier = Modifier
            .padding(padding)
            .background(color)
            .size(size - 2 * padding)
            .aspectRatio(1f),
        shape = RectangleShape,
        color = color,
        onClick = onClick
    ) {
    }
}

@Preview
@Composable
private fun AddCategoryDialogPreview() {
    MyFarmerPreview {
        AddCategoryDialog(
            state = AddCategoryDialogState(
                categoryName = "",
                selectedColor = Color(0xFF64B5F6),
                suggestions = listOf(
                    CategoryPopularity(name = "Fruits", count = 100),
                    CategoryPopularity(name = "Vegetables", count = 90),
                    CategoryPopularity(name = "Dairy", count = 80),
                )
            ),
            onCategoryNameChange = {},
            onColorSelected = {},
            onAdd = {},
            onDismiss = {}
        )
    }
}