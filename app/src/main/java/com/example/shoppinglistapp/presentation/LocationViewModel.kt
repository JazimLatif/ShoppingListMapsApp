package com.example.shoppinglistapp.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.BuildConfig
import com.example.shoppinglistapp.data.models.location.GeocodingResult
import com.example.shoppinglistapp.data.models.location.LocationData
import com.example.shoppinglistapp.domain.location.RetrofitClient
import kotlinx.coroutines.launch

class LocationViewModel: AndroidViewModel(application = Application()) {

    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address: State<List<GeocodingResult>> = _address

    fun updateLocation(newLocation: LocationData) {
        _location.value = newLocation
    }

    fun fetchAddress(latlng: String) {
        try {
            viewModelScope.launch {
                val result = RetrofitClient.create().getAddressFromCoordinates(
                    latlng,
                    BuildConfig.GOOGLE_MAPS_API_KEY
                )

                _address.value = result.results

            }
        } catch (e: Exception) {
            Log.e("res1", "${e.cause} - ${e.message.toString()}")
        }
    }
}