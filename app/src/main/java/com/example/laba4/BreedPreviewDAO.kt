package com.example.laba4

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BreedPreviewDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(breedPrev: BreedPreview)

    @Query("SELECT * FROM BreedPreviews")
    fun getAllBreedPreviews(): List<BreedPreview>

}