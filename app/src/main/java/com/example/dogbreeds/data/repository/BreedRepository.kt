package com.example.dogbreeds.data.repository

import com.example.dogbreeds.data.local.BreedDao
import com.example.dogbreeds.data.local.entity.BreedEntity
import com.example.dogbreeds.data.remote.RemoteDataSource
import com.example.dogbreeds.domain.model.Breed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BreedRepository(
    private val remote: RemoteDataSource,
    private val dao: BreedDao
) {

    val allBreeds: Flow<List<Breed>> = dao.observeAll().map { list ->
        list.map { it.toDomain() }
    }

    val favoriteBreeds: Flow<List<Breed>> = dao.observeFavorites().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun refreshBreeds() {
        val dtos = remote.fetchBreeds()
        val entities = dtos.map { dto ->
            BreedEntity(
                id = dto.id,
                name = dto.name,
                temperament = dto.temperament ?: "Unknown",
                origin = dto.origin,
                lifeSpan = dto.lifeSpan ?: "Unknown",
                description = dto.description,
                imageUrl = dto.image?.url ?: "",
                isFavorite = dao.getById(dto.id)?.isFavorite ?: false
            )
        }
        dao.insertAll(entities)
    }

    suspend fun toggleFavorite(breed: Breed) {
        val entity = dao.getById(breed.id) ?: return
        dao.updateBreed(entity.copy(isFavorite = !entity.isFavorite))
    }

    suspend fun getBreedById(id: Int): Breed? =
        dao.getById(id)?.toDomain()

    private fun BreedEntity.toDomain() = Breed(
        id = id,
        name = name,
        temperament = temperament,
        origin = origin,
        lifeSpan = lifeSpan,
        description = description,
        imageUrl = imageUrl,
        isFavorite = isFavorite
    )
}