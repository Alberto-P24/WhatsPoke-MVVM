package com.example.whatspoke.data.repository

import com.example.whatspoke.data.remote.PokeApiService
import com.example.whatspoke.data.remote.dto.PokemonDetailResponseDto
import com.example.whatspoke.data.remote.dto.PokemonListResponseDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonApiRepository @Inject constructor(
    private val pokeApiService: PokeApiService
) {

    suspend fun getPokemonList(limit: Int = 20, offset: Int = 0): PokemonListResponseDto {
        return pokeApiService.getPokemonList(limit, offset)
    }

    suspend fun getPokemonDetail(id: Int): PokemonDetailResponseDto {
        return pokeApiService.getPokemonDetail(id)
    }
}
