package com.example.dogbreeds.data

class RemoteDataSource(private val api: DogApiService) {
    suspend fun fetchBreeds(): List<BreedData> {
        return api.getBreeds().data
    }
}