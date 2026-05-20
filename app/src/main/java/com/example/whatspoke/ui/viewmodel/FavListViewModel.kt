package com.example.whatspoke.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatspoke.data.repository.FavoriteRepository
import com.example.whatspoke.model.Pokemon
import com.example.whatspoke.model.toPokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavListViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _favoritesState = MutableStateFlow<UiState<List<Pokemon>>>(UiState.Loading)
    val favoritesState: StateFlow<UiState<List<Pokemon>>> = _favoritesState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            favoriteRepository.getAllFavorites()
                .map { list ->
                    list.map { it.toPokemon() }
                }
                .collect { pokemonList ->
                    _favoritesState.value = UiState.Success(pokemonList)
                }
        }
    }

    fun deleteFavorite(id: Int) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(id)
        }
    }
}
