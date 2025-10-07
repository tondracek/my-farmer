package com.tondracek.myfarmer.ui.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.demo.domain.Demo
import com.tondracek.myfarmer.ui.core.theme.MyFarmerTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.UUID

@Composable
fun DemoScreen(
    state: DemoScreenState,
    onAddDemoClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDemoClick) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add demo"
                )
            }
        }
    ) { innerPadding ->
        when (state) {
            is DemoScreenState.Success -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = innerPadding
            ) {
                items(state.demos) {
                    Card(
                        Modifier.padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = it.name,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(it.date.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)))
                                Text(it.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                            }
                            Text(it.index.toString())
                            Text(
                                text = it.id.toString(),
                            )
                        }
                    }
                }
            }

            DemoScreenState.Loading -> CircularProgressIndicator()
            is DemoScreenState.Error -> Text(state.message)
        }
    }
}

@Preview
@Composable
private fun DemoScreenPreview() {
    val demos = listOf(
        Demo(
            id = UUID.randomUUID(),
            name = "Demo 1",
            index = 1,
            date = LocalDateTime.now()
        ),
        Demo(
            id = UUID.randomUUID(),
            name = "Demo 2",
            index = 2,
            date = LocalDateTime.now()
        ),
        Demo(
            id = UUID.randomUUID(),
            name = "Demo 3",
            index = 3,
            date = LocalDateTime.now()
        )
    )

    MyFarmerTheme {
        DemoScreen(
            state = DemoScreenState.Success(
                demos = demos
            ),
            onAddDemoClick = {}
        )
    }
}