package com.example.whatspoke.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_favorites")
data class PokemonEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val type: String,
    val height: Int = 0,
    val weight: Int = 0,
    val abilities: String = "",
    val stats: String = ""
)
