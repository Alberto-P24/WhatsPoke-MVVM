package com.example.whatspoke.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.whatspoke.R
import com.example.whatspoke.ui.components.PokemonCard
import com.example.whatspoke.ui.viewmodel.ElemListViewModel
import com.example.whatspoke.ui.viewmodel.UiState

@Composable
fun ElemListScreen(
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ElemListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val pokemonListState by viewModel.pokemonListState.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    when (val state = pokemonListState) {
        is UiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.error_loading_pokemon),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { viewModel.loadPokemonList() }) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }
        }
        is UiState.Success -> {
            val pokemonList = state.data
            if (pokemonList.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_pokemon_found))
                }
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(items = pokemonList, key = { it.id }) { pokemon ->
                        PokemonCard(
                            pokemon = pokemon,
                            isFavorite = favoriteIds.contains(pokemon.id),
                            onClick = { onNavigateToDetail(pokemon.id) },
                            onFavoriteClick = { viewModel.addToFavorites(pokemon) }
                        )
                    }
                }
            }
        }
    }
}
