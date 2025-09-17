package com.example.laba4

data class BreedData(
    val id: String,
    val breedName: String,
    val thumbnail: String,
    val description: String,
    val originCountry: String,
    val height: String,
    val weight: String,
    val photos: List<String>
)
