package com.tondracek.myfarmer.ui.common.layout

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.ui.common.button.GoBackButton
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.util.toUserFriendlyMessage

@Composable
fun ErrorLayout(
    modifier: Modifier = Modifier,
    failure: DomainResult.Failure,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current

    CardMessageLayout(
        modifier = modifier,
        cardColors = MyFarmerTheme.cardColors.error
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Default.Error,
            contentDescription = "Error Icon",
            tint = MyFarmerTheme.colors.error,
        )
        Text(
            modifier = Modifier.widthIn(max = 270.dp),
            text = failure.error.toUserFriendlyMessage(context),
            textAlign = TextAlign.Center,
        )
        GoBackButton(
            onNavigateBack = onNavigateBack,
            buttonColors = MyFarmerTheme.buttonColors.error
        )
    }
}

@Preview
@Composable
private fun ErrorLayoutPreview() {
    MyFarmerPreview {
        ErrorLayout(
            failure = DomainResult.Failure(ShopError.NotFound),
            onNavigateBack = {}
        )
    }
}