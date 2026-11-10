package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.sergio.lab7.ui.viewmodel.LocationDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsScreen(
    state: LocationDetailsUiState,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de location") },
                navigationIcon = { IconButton(onClick = onBack) { Text("←") } },
                actions = {
                    state.data?.let { location ->
                        IconButton(onClick = onFavoriteClick) {
                            Text(if (location.isFavorite) "★" else "☆", style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingLayout(onClick = onLoadingClick)
                state.hasError -> ErrorLayout(onRetry = onRetry)
                state.data == null -> Text("No se encontró esta location.", Modifier.padding(24.dp))
                else -> {
                    val location = state.data
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(location.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        LocationDetailRow("ID", location.id.toString())
                        LocationDetailRow("Nombre", location.name)
                        LocationDetailRow("Tipo", location.type)
                        LocationDetailRow("Dimensión", location.dimension)
                        LocationDetailRow("Favorita", if (location.isFavorite) "Sí" else "No")
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationDetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(label, modifier = Modifier.width(90.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium)
    }
}
