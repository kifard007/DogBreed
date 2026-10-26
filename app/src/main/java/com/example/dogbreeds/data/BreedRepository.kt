package com.example.dogbreeds.data

import com.example.dogbreeds.data.local.BreedDao
import com.example.dogbreeds.data.local.entity.BreedEntity
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
        val breedDataList = remote.fetchBreeds()
        val entities = breedDataList.map { data ->
            val attr = data.attributes
            val hashId = data.id.hashCode()
            BreedEntity(
                id = hashId,
                name = attr.name,
                temperament = attr.traits?.temperament?.joinToString(", ")
                    ?: "Информация отсутствует",
                origin = attr.origin?.country,
                lifeSpan = buildLifeSpan(attr.life),
                description = attr.description,
                imageUrl = attr.images?.firstOrNull()?.medium ?: "",
                isFavorite = dao.getById(hashId)?.isFavorite ?: false
            )
        }
        dao.insertAll(entities)
    }

    private fun buildLifeSpan(life: LifeSpan?): String {
        if (life?.min == null || life.max == null) return "Неизвестно"
        return "${life.min} - ${life.max} лет"
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