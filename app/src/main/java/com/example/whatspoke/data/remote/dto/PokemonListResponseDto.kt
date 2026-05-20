package com.example.whatspoke.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonListResponseDto(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<PokemonResultDto>
)

data class PokemonResultDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
