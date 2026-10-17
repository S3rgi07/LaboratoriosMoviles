package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import edu.sergio.lab7.R
import edu.sergio.lab7.data.Character
import edu.sergio.lab7.ui.viewmodel.CharactersUiState

@Composable
fun CharactersScreen(
    state: CharactersUiState,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    onFavoriteClick: (Character) -> Unit,
    onFavoritesFilterChange: (Boolean) -> Unit,
) {
    when {
        state.isLoading -> LoadingLayout(onClick = onLoadingClick)
        state.hasError -> ErrorLayout(onRetry = onRetry)
        else -> CharactersList(
            characters = state.data,
            showFavoritesOnly = state.showFavoritesOnly,
            onCharacterClick = onCharacterClick,
            onFavoriteClick = onFavoriteClick,
            onFavoritesFilterChange = onFavoritesFilterChange,
        )
    }
}

@Composable
private fun CharactersList(
    characters: List<Character>,
    showFavoritesOnly: Boolean,
    onCharacterClick: (Int) -> Unit,
    onFavoriteClick: (Character) -> Unit,
    onFavoritesFilterChange: (Boolean) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        FilterChip(
            selected = showFavoritesOnly,
            onClick = { onFavoritesFilterChange(!showFavoritesOnly) },
            label = { Text(stringResource(R.string.only_favorites)) },
            leadingIcon = { Text(if (showFavoritesOnly) "★" else "☆") },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        if (characters.isEmpty() && showFavoritesOnly) {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(stringResource(R.string.no_favorites))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 8.dp),
            ) {
                items(characters, key = { it.id }) { character ->
                    val favoriteAction = stringResource(
                        if (character.isFavorite) R.string.remove_favorite else R.string.add_favorite,
                    )
                    ListItem(
                        headlineContent = { Text(character.name) },
                        supportingContent = { Text("${character.species} · ${character.status}") },
                        leadingContent = { CharacterPortrait(character, Modifier.size(56.dp)) },
                        trailingContent = {
                            IconButton(onClick = { onFavoriteClick(character) }) {
                                Text(
                                    text = if (character.isFavorite) "★" else "☆",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = if (character.isFavorite) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    modifier = Modifier.semantics {
                                        contentDescription = "$favoriteAction ${character.name}"
                                    },
                                )
                            }
                        },
                        modifier = Modifier.clickable(
                            onClickLabel = stringResource(R.string.character_details),
                        ) { onCharacterClick(character.id) },
                    )
                }
            }
        }
    }
}
