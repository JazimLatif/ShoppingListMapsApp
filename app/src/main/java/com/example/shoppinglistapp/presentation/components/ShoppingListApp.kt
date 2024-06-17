package com.example.shoppinglistapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglistapp.domain.location.LocationUtils
import com.example.shoppinglistapp.presentation.LocationViewModel

@Composable
fun ShoppingListApp() {
    val navController = rememberNavController()
    val viewModel = LocationViewModel()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    NavHost(navController = navController, startDestination = "shoppinglistscreen") {
        composable("shoppinglistscreen") {
            ShoppingList(
                locationUtils = locationUtils,
                viewModel = viewModel,
                navController = navController,
                context = context,
                address = viewModel.address.value.firstOrNull()?.formatted_address ?: "No Address"
            )
        }

        dialog("locationscreen") { _ ->
            viewModel.location.value?.let { locationData ->
                LocationSelectionScreen(location = locationData, onLocationSelected = {
                    viewModel.fetchAddress("${it.latitude}, ${it.longitude}")
                    navController.popBackStack()
                })
            }
        }
    }
}