package com.example.dogbreeds.data.remote

import com.example.dogbreeds.data.remote.dto.BreedDto
import com.example.dogbreeds.data.remote.dto.BreedImageDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DogApiService {
    @GET("breeds")
    suspend fun getBreeds(): List<BreedDto>

    @GET("images/search")
    suspend fun getBreedImages(
        @Query("breed_ids") breedId: Int,
        @Query("limit") limit: Int = 20
    ): List<BreedImageDto>
}