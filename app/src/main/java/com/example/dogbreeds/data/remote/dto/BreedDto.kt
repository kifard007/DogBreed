package com.example.dogbreeds.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedDto(
    val id: Int,
    val name: String,
    val temperament: String?,
    val origin: String?,
    @Json(name = "life_span") val lifeSpan: String?,
    val description: String?,
    val image: BreedImageDto?
)

@JsonClass(generateAdapter = true)
data class BreedImageDto(
    val url: String?
)