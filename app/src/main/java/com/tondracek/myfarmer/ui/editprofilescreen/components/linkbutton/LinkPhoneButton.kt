package com.tondracek.myfarmer.ui.editprofilescreen.components.linkbutton

import android.util.Patterns
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview

@Composable
fun LinkPhoneButton(
    phone: String?,
    onSavePhone: (String?) -> Unit
) = LinkEditButton(
    input = phone,
    onSaveInput = onSavePhone,
    validator = { Patterns.PHONE.matcher(it).matches() },
    keyboardType = KeyboardType.Phone,
    color = Color(0xFF25D366),
    enterValidInputSupportingText = stringResource(R.string.invalid_phone_number),
    label = stringResource(R.string.phone_number),
    iconVector = Icons.Default.Phone,
    setLinkText = stringResource(R.string.add_phone_number),
    openPreview = null,
    prettifyInput = null,
)

@Preview
@Composable
private fun LinkPhoneButtonPreview() {
    MyFarmerPreview {
        LinkPhoneButton(
            phone = null,
            onSavePhone = {}
        )
    }
}

@Preview
@Composable
private fun LinkedPhoneButtonPreview() {
    MyFarmerPreview {
        LinkPhoneButton(
            phone = "+420 777 123 456",
            onSavePhone = {}
        )
    }
}
