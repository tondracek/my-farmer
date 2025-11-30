package com.tondracek.myfarmer.ui.createshopflow.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun DescriptionSection(
    description: String,
    onUpdateDescription: (String) -> Unit
) {
    Card(colors = MyFarmerTheme.cardColors.primary) {
        Column(
            modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
        ) {

            Card(colors = MyFarmerTheme.cardColors.onPrimary) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MyFarmerTheme.paddings.medium),
                    text = "Shop description",
                    style = MyFarmerTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = onUpdateDescription,
                minLines = 5,
                label = { Text("Describe your shop") }
            )
        }
    }
}
