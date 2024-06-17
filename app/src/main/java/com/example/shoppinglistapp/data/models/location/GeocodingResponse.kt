package com.example.shoppinglistapp.data.models.location

data class GeocodingResponse(
    val results: List<GeocodingResult>,
    val status: String
)