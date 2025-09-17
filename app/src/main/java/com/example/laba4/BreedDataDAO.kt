package com.example.laba4

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BreedDataDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(breed: BreedData)

    @Query("SELECT * FROM BreedsData")
    fun getAllBreeds(): List<BreedData>

    @Query("SELECT * FROM BreedsData WHERE id = :id LIMIT 1")
    fun getBreedById(id: String): BreedData?
}