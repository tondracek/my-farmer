package com.tondracek.myfarmer.ui.shopdetailscreen.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sectionlayout.ShopDetailSectionLayout
import java.time.DayOfWeek

@Composable
fun OpeningHoursSection(
    modifier: Modifier = Modifier,
    openingHours: OpeningHours,
) {
    if (openingHours is OpeningHours.Message && openingHours.message.isNullOrEmpty())
        return

    ShopDetailSectionLayout(
        modifier = modifier,
        title = stringResource(R.string.opening_hours),
    ) {
        when (openingHours) {
            is OpeningHours.Message -> itemNotNull(openingHours.message) {
                Text(
                    text = it,
                    modifier = Modifier.padding(8.dp)
                )
            }

            is OpeningHours.Time -> items(
                openingHours.dayToHours.toList().sortedBy { it.first.ordinal }
            ) { dayHours ->
                OpeningHoursItem(dayHours = dayHours)
            }
        }
    }
}

@Composable
private fun OpeningHoursItem(
    modifier: Modifier = Modifier,
    dayHours: Pair<DayOfWeek, String>,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MyFarmerTheme.paddings.small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = getDayOfWeekString(dayHours.first),
            style = MyFarmerTheme.typography.textMedium,
        )
        Text(
            text = dayHours.second,
            style = MyFarmerTheme.typography.textMedium,
        )
    }
}

@Composable
private fun getDayOfWeekString(dayOfWeek: DayOfWeek) = when (dayOfWeek) {
    DayOfWeek.MONDAY -> stringResource(R.string.monday)
    DayOfWeek.TUESDAY -> stringResource(R.string.tuesday)
    DayOfWeek.WEDNESDAY -> stringResource(R.string.wednesday)
    DayOfWeek.THURSDAY -> stringResource(R.string.thursday)
    DayOfWeek.FRIDAY -> stringResource(R.string.friday)
    DayOfWeek.SATURDAY -> stringResource(R.string.saturday)
    DayOfWeek.SUNDAY -> stringResource(R.string.sunday)
}

@Preview
@Composable
private fun OpeningHoursSchedulePreview() {
    MyFarmerPreview {
        OpeningHoursSection(
            openingHours = OpeningHours.Time(
                dayToHours = mapOf(
                    DayOfWeek.MONDAY to "8:00 - 10:00, 12:00 - 16:00",
                    DayOfWeek.TUESDAY to "8:00 - 16:00",
                    DayOfWeek.WEDNESDAY to "8:00 - 9:00, 14:00 - 18:00",
                    DayOfWeek.THURSDAY to "8:00 - 16:00",
                    DayOfWeek.FRIDAY to "8:00 - 16:00",
                    DayOfWeek.SATURDAY to "10:00 - 14:00",
                    DayOfWeek.SUNDAY to "Closed",
                )
            )
        )
    }
}

@Preview
@Composable
private fun OpeningHoursMessagePreview() {
    MyFarmerPreview {
        OpeningHoursSection(
            openingHours = OpeningHours.Message(
                message = "After prior arrangement (phone call or sms is fine)."
            )
        )
    }
}