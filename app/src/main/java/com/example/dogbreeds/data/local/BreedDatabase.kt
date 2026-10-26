package com.example.dogbreeds.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dogbreeds.data.local.entity.BreedEntity

@Database(entities = [BreedEntity::class], version = 1, exportSchema = false)
abstract class BreedDatabase : RoomDatabase() {
    abstract fun breedDao(): BreedDao
}