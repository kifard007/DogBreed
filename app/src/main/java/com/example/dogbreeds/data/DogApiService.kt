package com.example.dogbreeds.data

import com.squareup.moshi.Json
import retrofit2.http.GET
import com.squareup.moshi.JsonClass

data class DogCeoResponse(
    @Json(name = "message") val breedsMap: Map<String, List<String>>,
    val status: String
)
interface DogApiService {
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): DogCeoResponse
}