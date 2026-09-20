package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.sergio.lab7.R
import edu.sergio.lab7.data.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(characters: List<Character>, onCharacterClick: (Int) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.characters)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(characters, key = { it.id }) { character ->
                ListItem(
                    headlineContent = { Text(character.name) },
                    supportingContent = { Text("${character.species} · ${character.status}") },
                    leadingContent = { CharacterPortrait(character, Modifier.size(56.dp)) },
                    modifier = Modifier.clickable(
                        onClickLabel = stringResource(R.string.character_details),
                    ) { onCharacterClick(character.id) },
                )
            }
        }
    }
}
