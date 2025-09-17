package com.example.laba4

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BreedPreviewDAO {

    @Insert
    suspend fun insert(breedPrev: BreedPreview)

    @Query("SELECT * FROM BreedPreviews")
    suspend fun getAllBreedPreviews(): List<BreedPreview>

}