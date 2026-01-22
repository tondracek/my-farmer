package com.tondracek.myfarmer.ui.createshopflow.common.components.addcategorydialog

//fun NavGraphBuilder.addCategoryDialogDestination(
//    navController: NavController
//) = dialog<Route.AddCategoryDialog> {
//    val viewmodel: AddCategoryViewModel = hiltViewModel()
//    val state by viewmodel.state.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewmodel.effects.collect { event ->
//            when (event) {
//                is AddCategoryEffect.OnAddCategory -> {
//                    navController.setResult(
//                        NEW_CATEGORY_DIALOG_VALUE,
//                        event.category.toSerializable()
//                    )
//                    navController.navigateUp()
//                }
//
//                AddCategoryEffect.OnGoBackClicked ->
//                    navController.navigateUp()
//            }
//        }
//    }
//
//    AddCategoryDialog(
//        state = state,
//        onCategoryNameChange = viewmodel::onCategoryNameChange,
//        onColorSelected = viewmodel::onColorSelected,
//        onAdd = viewmodel::onAddCategory,
//        onDismiss = viewmodel::onDismissRequest
//    )
//}