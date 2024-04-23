package com.dev.diploma.domain.model

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String,
    val payment: String,
    val cvv: String,
    val term: String
) {
    constructor() : this("", "", "", "", "", "", "")
}