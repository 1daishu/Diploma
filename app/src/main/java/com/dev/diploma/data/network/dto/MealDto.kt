package com.dev.diploma.data.network.dto

import com.dev.diploma.domain.model.MealItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealDto(
    @Json(name = "meals")
    val meals: List<MealItem>
)