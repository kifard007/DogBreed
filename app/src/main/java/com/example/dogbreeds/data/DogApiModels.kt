package com.example.dogbreeds.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Корневой ответ: { "data": [ ... ] }
@JsonClass(generateAdapter = true)
data class BreedsResponse(
    val data: List<BreedData>
)

@JsonClass(generateAdapter = true)
data class BreedData(
    val id: String, // UUID породы
    val attributes: BreedAttributes
)

@JsonClass(generateAdapter = true)
data class BreedAttributes(
    val name: String,
    val description: String?,
    val life: LifeSpan?,
    val origin: Origin?,
    val images: List<BreedImage>?,
    val traits: Traits?
)

@JsonClass(generateAdapter = true)
data class LifeSpan(
    val min: Int?,
    val max: Int?
)

@JsonClass(generateAdapter = true)
data class Origin(
    val country: String?
)

@JsonClass(generateAdapter = true)
data class BreedImage(
    val url: String?,    // оригинал
    val medium: String?  // оптимизированное изображение (600px)
)

@JsonClass(generateAdapter = true)
data class Traits(
    val temperament: List<String>?
)