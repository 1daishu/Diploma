package com.dev.diploma.domain.model

data class MealItem(
    val calories: String,
    val carbohydrates: String,
    val fat: Int,
    val id: Int,
    val name: String,
    val photo_url: String,
    val protein: Int
)