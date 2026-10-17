package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.sergio.lab7.R
import edu.sergio.lab7.ui.viewmodel.CharacterDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    state: CharacterDetailsUiState,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val backLabel = stringResource(R.string.back)
    val addFavoriteLabel = stringResource(R.string.add_favorite)
    val removeFavoriteLabel = stringResource(R.string.remove_favorite)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.character_details)) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.semantics { contentDescription = backLabel }) {
                        Text("←", fontSize = 28.sp)
                    }
                },
                actions = {
                    val character = state.data
                    if (character != null) {
                        IconButton(onClick = onFavoriteClick) {
                            Text(
                                text = if (character.isFavorite) "★" else "☆",
                                fontSize = 28.sp,
                                modifier = Modifier.semantics {
                                    contentDescription = if (character.isFavorite) {
                                        "$removeFavoriteLabel ${character.name}"
                                    } else {
                                        "$addFavoriteLabel ${character.name}"
                                    }
                                },
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingLayout(onClick = onLoadingClick)
                state.hasError -> ErrorLayout(onRetry = onRetry)
                else -> Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val character = state.data
                    if (character == null) {
                        Text(stringResource(R.string.not_found))
                    } else {
                        CharacterPortrait(character, Modifier.size(184.dp))
                        Spacer(Modifier.height(20.dp))
                        Text(character.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(32.dp))
                        Column(Modifier.widthIn(max = 400.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                            DetailRow(stringResource(R.string.species), character.species)
                            DetailRow(stringResource(R.string.status), character.status)
                            DetailRow(stringResource(R.string.gender), character.gender)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(label, Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, Modifier.weight(1f), textAlign = TextAlign.End, fontWeight = FontWeight.Medium)
    }
}
