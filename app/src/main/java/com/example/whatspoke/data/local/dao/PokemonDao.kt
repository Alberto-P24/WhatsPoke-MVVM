package com.example.whatspoke.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.whatspoke.data.local.entity.PokemonEntity
import com.example.whatspoke.data.local.entity.PokemonWithComments
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon_favorites ORDER BY name ASC")
    fun getAllFavorites(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_favorites WHERE id = :id")
    suspend fun getFavoriteById(id: Int): PokemonEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(pokemon: PokemonEntity): Long

    @Query("DELETE FROM pokemon_favorites WHERE id = :id")
    suspend fun deleteFavoriteById(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM pokemon_favorites WHERE id = :id)")
    fun isFavoriteFlow(id: Int): Flow<Boolean>

    @Transaction
    @Query("SELECT * FROM pokemon_favorites WHERE id = :id")
    fun getPokemonWithComments(id: Int): Flow<PokemonWithComments?>
}
