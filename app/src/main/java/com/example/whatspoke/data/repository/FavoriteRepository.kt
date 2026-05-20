package com.example.whatspoke.data.repository

import com.example.whatspoke.data.local.dao.PokemonDao
import com.example.whatspoke.data.local.entity.PokemonEntity
import com.example.whatspoke.data.local.entity.PokemonWithComments
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val pokemonDao: PokemonDao
) {

    fun getAllFavorites(): Flow<List<PokemonEntity>> = pokemonDao.getAllFavorites()

    suspend fun getFavoriteById(id: Int): PokemonEntity? = pokemonDao.getFavoriteById(id)

    suspend fun addFavorite(pokemon: PokemonEntity): Boolean {
        val result = pokemonDao.insertFavorite(pokemon)
        return result != -1L
    }

    suspend fun removeFavorite(id: Int) {
        pokemonDao.deleteFavoriteById(id)
    }

    fun isFavoriteFlow(id: Int): Flow<Boolean> = pokemonDao.isFavoriteFlow(id)

    fun getPokemonWithComments(id: Int): Flow<PokemonWithComments?> = pokemonDao.getPokemonWithComments(id)
}
