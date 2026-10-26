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
        // Получаем список имён пород (например: "akita", "beagle")
        val breedNames = remote.fetchBreeds()

        // Превращаем каждое имя в BreedEntity для Room
        val entities = breedNames.map { name ->
            val hashId = name.hashCode()
            BreedEntity(
                id = hashId,
                name = name.replaceFirstChar { it.uppercase() },
                temperament = "Информация отсутствует",
                origin = null,
                lifeSpan = "Неизвестно",
                description = null,
                // Dog CEO отдаёт случайную картинку по имени породы
                imageUrl = "https://dog.ceo/api/breed/$name/images/random",
                isFavorite = dao.getById(hashId)?.isFavorite ?: false
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