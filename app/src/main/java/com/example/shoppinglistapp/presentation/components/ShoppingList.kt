package com.example.shoppinglistapp.presentation.components

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.shoppinglistapp.data.models.ShoppingItem
import com.example.shoppinglistapp.domain.location.LocationUtils
import com.example.shoppinglistapp.presentation.LocationViewModel
import com.example.shoppinglistapp.MainActivity


@Composable
fun ShoppingList(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    navController: NavController,
    context: Context,
    address: String
) {

    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permission ->
            when (permission[Manifest.permission.ACCESS_FINE_LOCATION] == true
                    && permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {

                true -> {

                }
                false -> {
                    // Ask for location
                    val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )

                    when (rationaleRequired) {
                        true -> Toast.makeText(
                            context,
                            "Location Permission is required for this feature to work",
                            Toast.LENGTH_SHORT
                        ).show()

                        false -> Toast.makeText(
                            context,
                            "Location Permission is required, please enable it in Android settings",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { showDialog = true}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems) {item ->
                if (item.isEditing) {
                    ShoppingItemEditor(item) { editedName, editedQuantity ->
                        sItems = sItems.map { it.copy(isEditing = false) }
                        val editedItem = sItems.find{ it.id == item.id }
                        editedItem?.let{
                            it.name = editedName
                            it.quantity = editedQuantity
                            it.address = address
                        }
                    }
                } else {
                    ShoppingItemCard(
                        item,
                        {
                            // finiding which item we have clicked the edit button on,
                            // and changing it's isEditing property to true if it matches the item id
                            sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                        },
                        {
                            sItems = sItems - item
//                            also can be done with filter
//                            sItems = sItems.filter {
//                                item.id != it.id
//                            }
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        if (itemName.isNotBlank()) {
                            val newItem = ShoppingItem(
                                id = sItems.size + 1,
                                name = itemName,
                                quantity = quantity.toInt(),
                                address = address
                            )
                            sItems += newItem
                            showDialog = false
                            itemName = ""
                            quantity = ""
                        }
                    }) {
                        Text("Add")
                    }
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            },
            title = { Text("Add Shopping Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Item name")},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = quantity,
                        label = { Text("Item quantity")},
                        onValueChange = { quantity = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    Button(onClick = {
                        if (locationUtils.hasLocationPermission(context)) {
                            locationUtils.requestLocationUpdates(viewModel)
                            navController.navigate("locationscreen") {
                                this.launchSingleTop
                            }
                        } else {
                            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) )
                        }
                    }) {
                        Text("Mark Address")
                    }
                }
            }
        )
    }
}

