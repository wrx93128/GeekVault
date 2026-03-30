package com.example.geekvault.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.geekvault.data.FavoriteCharacter
import com.example.geekvault.data.model.Character

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    favoriteIds: Set<Int> = emptySet(),
    onFavoriteClick: (FavoriteCharacter) -> Unit = {},
    onRemoveFavorite: (FavoriteCharacter) -> Unit = {}
) {
    when (val state = viewModel.uiState) {

        is HomeUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is HomeUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.characters) { character ->
                    CharacterCard(
                        character = character,
                        isFavorite = character.id in favoriteIds,
                        onFavoriteClick = onFavoriteClick,
                        onRemoveFavorite = onRemoveFavorite
                    )
                }
            }
        }

        is HomeUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = state.message, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.fetchCharacters() }) {
                    Text("Odśwież")
                }
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: Character,
    isFavorite: Boolean,
    onFavoriteClick: (FavoriteCharacter) -> Unit,
    onRemoveFavorite: (FavoriteCharacter) -> Unit
) {
    var showRemoveDialog by remember { mutableStateOf(false) }

    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Usuń z ulubionych") },
            text = { Text("Czy na pewno chcesz usunąć ${character.name} z ulubionych?") },
            confirmButton = {
                TextButton(onClick = {
                    showRemoveDialog = false
                    onRemoveFavorite(
                        FavoriteCharacter(
                            id = character.id,
                            name = character.name,
                            imageUrl = character.image
                        )
                    )
                }) {
                    Text("Tak")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Nie")
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = character.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${character.status} · ${character.species}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = {
                if (isFavorite) {
                    showRemoveDialog = true
                } else {
                    onFavoriteClick(
                        FavoriteCharacter(
                            id = character.id,
                            name = character.name,
                            imageUrl = character.image
                        )
                    )
                }
            }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Ulubione",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
