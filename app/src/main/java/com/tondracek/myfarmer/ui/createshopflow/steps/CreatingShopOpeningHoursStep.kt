package com.tondracek.myfarmer.ui.createshopflow.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.ui.common.openinghours.getDayOfWeekString
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.preview.PreviewDark
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.components.NavigationButtons
import java.time.DayOfWeek

enum class OpeningHoursMode { DAY_TO_DAY, MESSAGE }

@Composable
fun CreatingShopOpeningHoursStep(
    shopInput: ShopInput,
    onUpdateOpeningHours: (OpeningHours) -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Content(
            shopInput = shopInput,
            onUpdateOpeningHours = onUpdateOpeningHours
        )

        NavigationButtons(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(MyFarmerTheme.paddings.bottomButtons),
            onNext = onNextStep,
            onPrevious = onPreviousStep,
        )
    }
}

@Composable
private fun Content(
    shopInput: ShopInput,
    onUpdateOpeningHours: (OpeningHours) -> Unit
) {
    var mode by remember {
        mutableStateOf(
            when (shopInput.openingHours) {
                is OpeningHours.Time -> OpeningHoursMode.DAY_TO_DAY
                is OpeningHours.Message -> OpeningHoursMode.MESSAGE
            }
        )
    }

    var dayInput: Map<DayOfWeek, String> by remember {
        mutableStateOf(
            mapOf(
                DayOfWeek.MONDAY to "",
                DayOfWeek.TUESDAY to "",
                DayOfWeek.WEDNESDAY to "",
                DayOfWeek.THURSDAY to "",
                DayOfWeek.FRIDAY to "",
                DayOfWeek.SATURDAY to "",
                DayOfWeek.SUNDAY to "",
            )
        )
    }

    var messageInput by remember { mutableStateOf("") }

    LaunchedEffect(shopInput.openingHours) {
        when (shopInput.openingHours) {
            is OpeningHours.Time -> dayInput = shopInput.openingHours.dayToHours
            is OpeningHours.Message -> messageInput = shopInput.openingHours.message
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MyFarmerTheme.paddings.medium),
        colors = MyFarmerTheme.cardColors.base
    ) {
        Column(
            modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.opening_hours),
                style = MyFarmerTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            ModeRow(mode = mode, onModeChange = {
                mode = it
                onUpdateOpeningHours(
                    when (it) {
                        OpeningHoursMode.DAY_TO_DAY -> OpeningHours.Time(dayToHours = dayInput)
                        OpeningHoursMode.MESSAGE -> OpeningHours.Message(message = messageInput)
                    }
                )
            })

            when (mode) {
                OpeningHoursMode.DAY_TO_DAY -> DayToDayOpeningHoursInput(
                    dayInput = dayInput,
                    onDayInputChange = {
                        dayInput = it
                        onUpdateOpeningHours(OpeningHours.Time(dayToHours = it))
                    }
                )

                OpeningHoursMode.MESSAGE -> MessageOpeningHoursInput(
                    messageInput = messageInput,
                    onMessageInputChange = {
                        messageInput = it
                        onUpdateOpeningHours(OpeningHours.Message(message = it))
                    }
                )
            }
        }
    }
}

@Composable
private fun ModeRow(
    modifier: Modifier = Modifier,
    mode: OpeningHoursMode,
    onModeChange: (OpeningHoursMode) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
    ) {
        InputChip(
            label = { Text(stringResource(R.string.day_to_day)) },
            selected = mode == OpeningHoursMode.DAY_TO_DAY,
            onClick = { onModeChange(OpeningHoursMode.DAY_TO_DAY) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Calendar Icon"
                )
            }
        )
        InputChip(
            label = { Text(stringResource(R.string.message)) },
            selected = mode == OpeningHoursMode.MESSAGE,
            onClick = { onModeChange(OpeningHoursMode.MESSAGE) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Message,
                    contentDescription = "Message Icon"
                )
            }
        )
    }
}

@Composable
private fun DayToDayOpeningHoursInput(
    modifier: Modifier = Modifier,
    dayInput: Map<DayOfWeek, String>,
    onDayInputChange: (Map<DayOfWeek, String>) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.smallMedium),
    ) {
        DayOfWeek.entries.forEach { day ->
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = dayInput[day] ?: "",
                onValueChange = {
                    val newMap = dayInput.toMutableMap()
                    newMap[day] = it
                    onDayInputChange(newMap)
                },
                label = {
                    Text(stringResource(R.string.write_message_for_x_here, getDayOfWeekString(day)))
                },
            )
        }
    }
}

@Composable
private fun MessageOpeningHoursInput(
    modifier: Modifier = Modifier,
    messageInput: String,
    onMessageInputChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = messageInput,
        onValueChange = onMessageInputChange,
        label = { Text(stringResource(R.string.enter_opening_hours_message)) },
        singleLine = false,
        maxLines = 5,
        supportingText = { Text(stringResource(R.string.opening_hours_message_example)) }
    )
}

@PreviewDark
@Composable
private fun CreatingShopOpeningHoursStepPreview() {
    MyFarmerPreview {
        CreatingShopOpeningHoursStep(
            shopInput = shop0.toShopInput(),
            onUpdateOpeningHours = { },
            onNextStep = { },
            onPreviousStep = { }
        )
    }
}