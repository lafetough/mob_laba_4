package com.example.laba4

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [BreedData::class, BreedPreview::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breedDataDao(): BreedDataDAO
    abstract fun breedPreviewDao(): BreedPreviewDAO

    // Синглтон
    companion object {
        // Чтобы изменения были видны из всех потоков
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Возвращаем инстанс, если нет - блокируем одновременный доступ  и строим базу
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}