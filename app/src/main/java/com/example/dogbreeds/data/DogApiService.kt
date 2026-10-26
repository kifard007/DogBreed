package com.example.dogbreeds.data

import retrofit2.http.GET
import retrofit2.http.Query

interface DogApiService {
    @GET("breeds")
    suspend fun getBreeds(
        @Query("page[size]") pageSize: Int = 10
    ): BreedsResponse
}