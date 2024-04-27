package com.dev.diploma.domain.model

data class Product(
    val id: Int,
    val name: String,
    val calories: String,
    val protein: Int,
    val fat: Int,
    val carbohydrates: Int,
    val photo_url: String
) {
    constructor() : this(0, "", "", 0, 0, 0, "")
}
