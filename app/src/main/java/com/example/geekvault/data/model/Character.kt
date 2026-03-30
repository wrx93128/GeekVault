package com.example.geekvault.data.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String
)

data class ApiResponse(
    val results: List<Character>
)
