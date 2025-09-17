package com.example.laba4
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BreedPreviews")
data class BreedPreview (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "breed_name")
    val breedName: String,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String
)