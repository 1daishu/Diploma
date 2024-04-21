package com.dev.diploma.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealItmDto(
    @Json(name = "calories")
    val calories: Int,
    @Json(name = "carbohydrates")
    val carbohydrates: Int,
    @Json(name = "fat")
    val fat: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "photo_url")
    val photo_url: String,
    @Json(name = "protein")
    val protein: Int
)
