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
class DetailItemViewModel @Inject constructor(
    private val pokemonApiRepository: PokemonApiRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _pokemonDetailState = MutableStateFlow<UiState<Pokemon>>(UiState.Loading)
    val pokemonDetailState: StateFlow<UiState<Pokemon>> = _pokemonDetailState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow: SharedFlow<String> = _eventFlow.asSharedFlow()

    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            _pokemonDetailState.value = UiState.Loading
            try {
                val detail = pokemonApiRepository.getPokemonDetail(id)
                _pokemonDetailState.value = UiState.Success(detail.toPokemon())
            } catch (e: Exception) {
                _pokemonDetailState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
        viewModelScope.launch {
            favoriteRepository.isFavoriteFlow(id).collect { fav ->
                _isFavorite.value = fav
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentPokemon = (_pokemonDetailState.value as? UiState.Success<Pokemon>)?.data ?: return@launch
            val exists = favoriteRepository.getFavoriteById(currentPokemon.id) != null
            if (exists) {
                _eventFlow.emit("Este Pokemon ya es favorito")
            } else {
                favoriteRepository.addFavorite(currentPokemon.toEntity())
                _isFavorite.value = true
                _eventFlow.emit("Pokemon guardado en favoritos")
            }
        }
    }
}
