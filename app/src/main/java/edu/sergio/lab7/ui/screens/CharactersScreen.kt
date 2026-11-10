package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.sergio.lab7.data.Character
import edu.sergio.lab7.ui.viewmodel.CharactersUiState

@Composable
fun CharactersScreen(
    state: CharactersUiState,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    onCreate: (String, String, String, String, String) -> Unit,
) {
    var creating by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        when {
            state.isLoading -> LoadingLayout(onClick = onLoadingClick)
            state.hasError -> ErrorLayout(onRetry = onRetry)
            else -> CharacterList(state.data, onCharacterClick)
        }
        FloatingActionButton(
            onClick = { creating = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
        ) { Text("+") }
    }
    if (creating) {
        CharacterEditorDialog(
            title = "Crear personaje",
            initial = null,
            onDismiss = { creating = false },
            onSave = { name, status, species, gender, image ->
                onCreate(name, status, species, gender, image)
                creating = false
            },
        )
    }
}

@Composable
private fun CharacterList(characters: List<Character>, onCharacterClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 88.dp),
    ) {
        items(characters, key = { it.id }) { character ->
            ListItem(
                headlineContent = { Text(character.name) },
                supportingContent = { Text("${character.species} · ${character.status}") },
                leadingContent = { CharacterPortrait(character, Modifier.size(56.dp)) },
                modifier = Modifier.clickable { onCharacterClick(character.id) },
            )
        }
    }
}

@Composable
fun CharacterEditorDialog(
    title: String,
    initial: Character?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit,
) {
    var name by remember(initial) { mutableStateOf(initial?.name.orEmpty()) }
    var status by remember(initial) { mutableStateOf(initial?.status ?: "Alive") }
    var species by remember(initial) { mutableStateOf(initial?.species ?: "Human") }
    var gender by remember(initial) { mutableStateOf(initial?.gender ?: "unknown") }
    var image by remember(initial) { mutableStateOf(initial?.image.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, singleLine = true)
                OutlinedTextField(status, { status = it }, label = { Text("Estado") }, singleLine = true)
                OutlinedTextField(species, { species = it }, label = { Text("Especie") }, singleLine = true)
                OutlinedTextField(gender, { gender = it }, label = { Text("Género") }, singleLine = true)
                OutlinedTextField(image, { image = it }, label = { Text("URL de imagen (opcional)") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, status, species, gender, image) },
                enabled = name.isNotBlank() && status.isNotBlank() && species.isNotBlank() && gender.isNotBlank(),
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}
