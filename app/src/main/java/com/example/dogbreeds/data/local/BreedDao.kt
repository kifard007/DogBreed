package com.example.dogbreeds.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dogbreeds.data.local.entity.BreedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {

    @Query("SELECT * FROM breeds ORDER BY name ASC")
    fun observeAll(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE isFavorite = 1")
    fun observeFavorites(): Flow<List<BreedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedEntity>)

    @Update
    suspend fun updateBreed(breed: BreedEntity)

    @Query("SELECT * FROM breeds WHERE id = :id")
    suspend fun getById(id: Int): BreedEntity?

    @Query("DELETE FROM breeds")
    suspend fun clearAll()
}