package com.tondracek.myfarmer.ui.editprofilescreen.components.linkbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun LinkActionButton(
    text: String,
    isSet: Boolean,
    onEdit: () -> Unit,
    setLinkText: String,
    iconVector: ImageVector,
    color: Color,
    prettifyInput: (String) -> String,
) {
    Button(
        colors = MyFarmerTheme.buttonColors.custom(color),
        onClick = onEdit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
            )

            Text(
                text = when (isSet) {
                    true -> prettifyInput(text)
                    false -> setLinkText
                }
            )

            if (isSet)
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Icon",
                )
        }
    }
}
