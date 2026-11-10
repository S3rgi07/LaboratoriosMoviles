package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.sergio.lab7.ui.viewmodel.CharacterDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    state: CharacterDetailsUiState,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onUpdate: (String, String, String, String, String) -> Unit,
    onDelete: () -> Unit,
) {
    var editing by remember { mutableStateOf(false) }
    var confirmingDelete by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del personaje") },
                navigationIcon = { IconButton(onClick = onBack) { Text("←") } },
                actions = {
                    if (state.data != null) {
                        TextButton(onClick = { editing = true }) { Text("Editar") }
                        TextButton(onClick = { confirmingDelete = true }) { Text("Eliminar") }
                    }
                },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingLayout(onClick = onLoadingClick)
                state.hasError -> ErrorLayout(onRetry = onRetry)
                state.data == null -> Text("No se encontró este personaje.", Modifier.padding(24.dp))
                else -> {
                    val character = state.data
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CharacterPortrait(character, Modifier.size(184.dp))
                        Spacer(Modifier.height(20.dp))
                        Text(character.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(32.dp))
                        Column(Modifier.widthIn(max = 400.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                            DetailRow("Especie", character.species)
                            DetailRow("Estado", character.status)
                            DetailRow("Género", character.gender)
                        }
                    }
                }
            }
        }
    }
    val character = state.data
    if (editing && character != null) {
        CharacterEditorDialog(
            title = "Editar personaje",
            initial = character,
            onDismiss = { editing = false },
            onSave = { name, status, species, gender, image ->
                onUpdate(name, status, species, gender, image)
                editing = false
            },
        )
    }
    if (confirmingDelete) {
        AlertDialog(
            onDismissRequest = { confirmingDelete = false },
            title = { Text("Eliminar personaje") },
            text = { Text("Este cambio se guardará localmente. ¿Deseas continuar?") },
            confirmButton = { Button(onClick = onDelete) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { confirmingDelete = false }) { Text("Cancelar") } },
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(label, Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, Modifier.weight(1f), textAlign = TextAlign.End, fontWeight = FontWeight.Medium)
    }
}
