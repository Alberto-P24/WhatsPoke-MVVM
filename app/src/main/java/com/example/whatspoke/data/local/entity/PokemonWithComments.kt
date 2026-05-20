package com.example.whatspoke.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PokemonWithComments(
    @Embedded
    val pokemon: PokemonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "pokemonId"
    )
    val comments: List<CommentEntity>
)
