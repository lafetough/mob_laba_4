package com.example.laba4
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BreedsData")
data class BreedData(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "breed_name")
    val breedName: String,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "origin_country")
    val originCountry: String,
    @ColumnInfo(name = "height")
    val height: String,
    @ColumnInfo(name = "weight")
    val weight: String,
    @ColumnInfo(name = "photos")
    val photos: List<String>
)
