package com.example.whatspoke.model

import com.example.whatspoke.data.local.entity.PokemonEntity
import com.example.whatspoke.data.remote.dto.PokemonDetailResponseDto
import com.example.whatspoke.data.remote.dto.PokemonResultDto

data class Pokemon(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val type: String,
    val height: Int = 0,
    val weight: Int = 0,
    val abilities: String = "",
    val stats: String = "",
    val isFavorite: Boolean = false
)

fun PokemonResultDto.toPokemon(): Pokemon {
    val id = this.url.trimEnd('/').split("/").last().toIntOrNull() ?: 0
    return Pokemon(
        id = id,
        name = this.name.replaceFirstChar { it.uppercase() },
        description = "",
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
        type = ""
    )
}

fun PokemonDetailResponseDto.toPokemon(): Pokemon {
    val typeString = this.types.joinToString(", ") { it.type.name.replaceFirstChar { c -> c.uppercase() } }
    val abilitiesString = this.abilities.joinToString(", ") { it.ability.name.replaceFirstChar { c -> c.uppercase() } }
    val statsString = this.stats.joinToString("\n") { "${it.stat.name.replaceFirstChar { c -> c.uppercase() }}: ${it.baseStat}" }
    return Pokemon(
        id = this.id,
        name = this.name.replaceFirstChar { it.uppercase() },
        description = "A Pokemon of type $typeString. Height: ${this.height / 10.0}m, Weight: ${this.weight / 10.0}kg.",
        imageUrl = this.sprites.frontDefault
            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${this.id}.png",
        type = typeString,
        height = this.height,
        weight = this.weight,
        abilities = abilitiesString,
        stats = statsString
    )
}

fun PokemonEntity.toPokemon(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        type = this.type,
        height = this.height,
        weight = this.weight,
        abilities = this.abilities,
        stats = this.stats,
        isFavorite = true
    )
}

fun Pokemon.toEntity(): PokemonEntity {
    return PokemonEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        type = this.type,
        height = this.height,
        weight = this.weight,
        abilities = this.abilities,
        stats = this.stats
    )
}
