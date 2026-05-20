package com.example.whatspoke.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.whatspoke.R
import com.example.whatspoke.model.Pokemon
import com.example.whatspoke.ui.components.PokemonCard
import com.example.whatspoke.ui.viewmodel.FavListViewModel
import com.example.whatspoke.ui.viewmodel.UiState

@Composable
fun FavListScreen(
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavListViewModel = hiltViewModel()
) {
    val favoritesState by viewModel.favoritesState.collectAsStateWithLifecycle()
    var pokemonToDelete by remember { mutableStateOf<Pokemon?>(null) }

    when (val state = favoritesState) {
        is UiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Success -> {
            val favPokemonList = state.data
            if (favPokemonList.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_favorites_yet))
                }
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(favPokemonList, key = { it.id }) { pokemon ->
                        PokemonCard(
                            pokemon = pokemon,
                            isFavorite = true,
                            onClick = { onNavigateToDetail(pokemon.id) },
                            onFavoriteClick = { pokemonToDelete = pokemon }
                        )
                    }
                }
            }
        }
        else -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.no_favorites_yet))
            }
        }
    }

    if (pokemonToDelete != null) {
        AlertDialog(
            onDismissRequest = { pokemonToDelete = null },
            title = { Text(stringResource(R.string.delete_confirmation_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.delete_confirmation_message,
                        pokemonToDelete!!.name
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteFavorite(pokemonToDelete!!.id)
                        pokemonToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { pokemonToDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
