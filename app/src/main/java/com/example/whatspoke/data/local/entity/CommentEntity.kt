package com.example.whatspoke.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["pokemonId"])]
)
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val commentId: Long = 0,
    val pokemonId: Int,
    val authorName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
