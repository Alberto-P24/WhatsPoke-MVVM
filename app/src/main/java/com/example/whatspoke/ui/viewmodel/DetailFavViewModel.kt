package com.example.whatspoke.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatspoke.data.local.entity.CommentEntity
import com.example.whatspoke.data.local.entity.PokemonWithComments
import com.example.whatspoke.data.repository.CommentRepository
import com.example.whatspoke.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFavViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val _pokemonWithComments = MutableStateFlow<PokemonWithComments?>(null)
    val pokemonWithComments: StateFlow<PokemonWithComments?> = _pokemonWithComments.asStateFlow()

    private val _comments = MutableStateFlow<List<CommentEntity>>(emptyList())
    val comments: StateFlow<List<CommentEntity>> = _comments.asStateFlow()

    fun loadPokemonWithComments(id: Int) {
        viewModelScope.launch {
            favoriteRepository.getPokemonWithComments(id).collect { data ->
                _pokemonWithComments.value = data
                _comments.value = data?.comments ?: emptyList()
            }
        }
    }

    fun addComment(pokemonId: Int, authorName: String, content: String) {
        viewModelScope.launch {
            val comment = CommentEntity(
                pokemonId = pokemonId,
                authorName = authorName,
                content = content
            )
            commentRepository.addComment(comment)
        }
    }

    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            commentRepository.deleteComment(commentId)
        }
    }
}
