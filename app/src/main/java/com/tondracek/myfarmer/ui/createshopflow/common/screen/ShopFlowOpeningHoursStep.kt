package com.tondracek.myfarmer.ui.createshopflow.common.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.tondracek.myfarmer.ui.common.lazycolumn.fadingEdges
import com.tondracek.myfarmer.ui.common.openinghours.getDayOfWeekString
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.preview.PreviewDark
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFormEvent
import java.time.DayOfWeek

enum class OpeningHoursMode {
    DAY_TO_DAY, MESSAGE;

    companion object {
        fun fromOpeningHours(openingHours: OpeningHours) = when (openingHours) {
            is OpeningHours.Time -> DAY_TO_DAY
            is OpeningHours.Message -> MESSAGE
        }
    }
}

@Composable
fun ShopFlowOpeningHoursStep(
    shopInput: ShopInput,
    onShopFormEvent: (ShopFormEvent) -> Unit,
) {
    fun onUpdateOpeningHours(newOpeningHours: OpeningHours) {
        onShopFormEvent(ShopFormEvent.UpdateOpeningHours(newOpeningHours))
    }

    var savedDayInput: Map<DayOfWeek, String> by rememberSaveable { mutableStateOf(emptyMap()) }
    var savedMessageInput by rememberSaveable { mutableStateOf("") }

    fun loadSavedInput(mode: OpeningHoursMode) {
        val newOpeningHours = when (mode) {
            OpeningHoursMode.DAY_TO_DAY -> OpeningHours.Time(dayToHours = savedDayInput)
            OpeningHoursMode.MESSAGE -> OpeningHours.Message(message = savedMessageInput)
        }
        onUpdateOpeningHours(newOpeningHours)
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

            ModeRow(
                mode = OpeningHoursMode.fromOpeningHours(shopInput.openingHours),
                onModeChange = { loadSavedInput(it) }
            )

            when (shopInput.openingHours) {
                is OpeningHours.Message -> MessageOpeningHoursInput(
                    messageInput = shopInput.openingHours.message,
                    onMessageInputChange = {
                        savedMessageInput = it
                        onUpdateOpeningHours(OpeningHours.Message(it))
                    }
                )

                is OpeningHours.Time -> DayToDayOpeningHoursInput(
                    dayInput = shopInput.openingHours.dayToHours,
                    onDayInputChange = {
                        savedDayInput = it
                        onUpdateOpeningHours(OpeningHours.Time(it))
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
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fadingEdges(listState),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.smallMedium),
    ) {
        items(DayOfWeek.entries) { day ->
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
                singleLine = true,
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
        ShopFlowOpeningHoursStep(
            shopInput = shop0.toShopInput(),
            onShopFormEvent = {},
        )
    }
}