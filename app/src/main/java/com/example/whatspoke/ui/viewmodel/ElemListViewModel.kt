package com.example.whatspoke.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatspoke.data.repository.FavoriteRepository
import com.example.whatspoke.data.repository.PokemonApiRepository
import com.example.whatspoke.model.Pokemon
import com.example.whatspoke.model.toEntity
import com.example.whatspoke.model.toPokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElemListViewModel @Inject constructor(
    private val pokemonApiRepository: PokemonApiRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _pokemonListState = MutableStateFlow<UiState<List<Pokemon>>>(UiState.Loading)
    val pokemonListState: StateFlow<UiState<List<Pokemon>>> = _pokemonListState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow: SharedFlow<String> = _eventFlow.asSharedFlow()

    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()

    init {
        loadPokemonList()
        observeFavoriteIds()
    }

    private fun observeFavoriteIds() {
        viewModelScope.launch {
            favoriteRepository.getAllFavorites().collect { favorites ->
                _favoriteIds.value = favorites.map { it.id }.toSet()
            }
        }
    }

    fun loadPokemonList() {
        viewModelScope.launch {
            _pokemonListState.value = UiState.Loading
            try {
                val response = pokemonApiRepository.getPokemonList(limit = 20)
                val pokemonList = response.results.map { it.toPokemon() }
                val pokemonWithDetails = pokemonList.map { pokemon ->
                    try {
                        val detail = pokemonApiRepository.getPokemonDetail(pokemon.id)
                        detail.toPokemon().copy(isFavorite = pokemon.isFavorite)
                    } catch (e: Exception) {
                        pokemon
                    }
                }
                _pokemonListState.value = UiState.Success(pokemonWithDetails)
            } catch (e: Exception) {
                _pokemonListState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun addToFavorites(pokemon: Pokemon) {
        viewModelScope.launch {
            val exists = favoriteRepository.getFavoriteById(pokemon.id) != null
            if (exists) {
                _eventFlow.emit("Este Pokemon ya es favorito")
            } else {
                favoriteRepository.addFavorite(pokemon.toEntity())
                _eventFlow.emit("Pokemon guardado en favoritos")
            }
        }
    }
}
