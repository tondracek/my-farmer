package com.tondracek.myfarmer.ui.createshopflow.components.addcategorydialog

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.stefanoq21.material3.navigation.bottomSheet
import com.tondracek.myfarmer.shopcategory.domain.model.toSerializable
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.setResult

fun NavGraphBuilder.addCategoryDialogDestination(
    navController: NavController
) = bottomSheet<Route.AddCategoryDialog> {
    val viewmodel: AddCategoryViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.effects.collect { event ->
            when (event) {
                is AddCategoryEffect.OnAddCategory -> {
                    navController.setResult(
                        NEW_CATEGORY_DIALOG_VALUE,
                        event.category.toSerializable()
                    )
                    navController.navigateUp()
                }

                AddCategoryEffect.OnGoBackClicked ->
                    navController.navigateUp()
            }
        }
    }

    AddCategoryDialog(
        state = state,
        onCategoryNameChange = viewmodel::onCategoryNameChange,
        onColorSelected = viewmodel::onColorSelected,
        onAdd = viewmodel::onAddCategory,
        onDismiss = viewmodel::onDismissRequest
    )
}