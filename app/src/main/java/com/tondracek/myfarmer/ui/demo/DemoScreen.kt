package com.tondracek.myfarmer.ui.demo

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tondracek.myfarmer.demo.domain.Demo

@Composable
fun DemoScreen(
    items: List<Demo>,
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
        LazyColumn(contentPadding = innerPadding) {
            items(items) {
                Card {
                    Text(it.name)
                    Text(it.date.toString())
                    Text(it.index.toString())
                    Text(it.id.toString())
                }
            }
        }
    }
}