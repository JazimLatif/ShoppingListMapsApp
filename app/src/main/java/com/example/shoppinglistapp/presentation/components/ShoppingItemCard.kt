package com.example.shoppinglistapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shoppinglistapp.data.models.ShoppingItem


@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick:() -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xFF018786)),
                shape = RoundedCornerShape(8.dp),
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(8.dp)) {
            Row {
                Text(
                    item.name,
                    Modifier.padding(8.dp)
                )
                Text(
                    "Qty: ${item.quantity}",
                    Modifier.padding(8.dp)
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.LocationOn, "location icon")
                Text(item.address)
            }
        }

        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(
                onClick = onEditClick
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit button for ${item.name}")
            }
            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete button for ${item.name}")
            }
        }
    }
}