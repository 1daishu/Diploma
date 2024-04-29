package com.dev.diploma.domain.model

data class Product(
    val id: Int,
    val name: String,
    val calories: String,
    val protein: Int,
    val fat: Int,
    val carbohydrates: String,
    val photo_url: String,
    val quantity: String,
    val time: String
) {
    constructor() : this(0, "", "", 0, 0, "", "", "", "")
}
