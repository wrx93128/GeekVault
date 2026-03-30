package com.example.geekvault.data.remote

import com.example.geekvault.data.model.ApiResponse
import retrofit2.http.GET

interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(): ApiResponse
}
