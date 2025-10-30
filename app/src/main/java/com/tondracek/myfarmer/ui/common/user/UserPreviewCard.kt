package com.tondracek.myfarmer.ui.common.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.systemuser.data.user2
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.core.preview.AsyncImagePreviewFix
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun UserPreviewCard(
    modifier: Modifier = Modifier,
    user: SystemUser,
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ImageView(
                modifier = Modifier
                    .size(48.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape),
                imageResource = user.profilePicture
            )
            Text(
                modifier = Modifier
                    .widthIn(max = 180.dp)
                    .padding(horizontal = MyFarmerTheme.paddings.extraSmall),
                text = user.name,
                style = MyFarmerTheme.typography.headerSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun UserPreviewCardPreview() {
    AsyncImagePreviewFix {
        MyFarmerTheme {
            UserPreviewCard(user = user2)
        }
    }
}