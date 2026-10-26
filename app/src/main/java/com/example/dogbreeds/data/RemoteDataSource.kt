package com.example.dogbreeds.data

class RemoteDataSource(private val api: DogApiService) {
    suspend fun fetchBreeds(): List<String> {
        return api.getAllBreeds().breedsMap.keys.toList()
    }
}