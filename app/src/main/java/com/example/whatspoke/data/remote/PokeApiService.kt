package com.example.whatspoke.data.remote

import com.example.whatspoke.data.remote.dto.PokemonDetailResponseDto
import com.example.whatspoke.data.remote.dto.PokemonListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponseDto

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailResponseDto
}
