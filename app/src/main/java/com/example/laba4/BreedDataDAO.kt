package com.example.laba4

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BreedDataDAO {

    @Insert
    suspend fun insert(breed: BreedData)

    @Query("SELECT * FROM BreedsData")
    suspend fun getAllBreeds(): List<BreedData>

    @Query("SELECT * FROM BreedsData WHERE id = :id LIMIT 1")
    suspend fun getBreedById(id: String): BreedData?
}