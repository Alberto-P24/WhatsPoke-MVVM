package com.example.whatspoke.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import coil.compose.AsyncImage
import com.example.whatspoke.R
import com.example.whatspoke.ui.viewmodel.DetailItemViewModel
import com.example.whatspoke.ui.viewmodel.UiState

@Composable
fun DetailItemScreen(
    pokemonId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailItemViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val pokemonDetailState by viewModel.pokemonDetailState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemonDetail(pokemonId)
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    when (val state = pokemonDetailState) {
        is UiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { viewModel.loadPokemonDetail(pokemonId) }) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }
        }
        is UiState.Success -> {
            val pokemon = state.data
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = pokemon.imageUrl,
                        contentDescription = pokemon.name,
                        modifier = Modifier.size(180.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = stringResource(R.string.type_label, pokemon.type),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = pokemon.description,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.height_label, pokemon.height.toDouble() / 10.0),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.weight_label, pokemon.weight.toDouble() / 10.0),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.abilities_label, pokemon.abilities),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.stats_label),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = pokemon.stats,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.toggleFavorite() },
                    modifier = Modifier.align(Alignment.End),
                    enabled = !isFavorite
                ) {
                    Text(stringResource(R.string.save_to_favorites))
                }
            }
        }
    }
}
