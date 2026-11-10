package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.sergio.lab7.data.Location
import edu.sergio.lab7.ui.viewmodel.LocationFilter
import edu.sergio.lab7.ui.viewmodel.LocationsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    state: LocationsUiState,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit,
    onLocationClick: (Int) -> Unit,
    onFavoriteClick: (Location) -> Unit,
    onFilterChange: (LocationFilter) -> Unit,
) {
    when {
        state.isLoading -> LoadingLayout(onClick = onLoadingClick)
        state.hasError -> ErrorLayout(onRetry = onRetry)
        else -> Column(Modifier.fillMaxSize()) {
            SingleChoiceSegmentedButtonRow(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                LocationFilter.entries.forEachIndexed { index, filter ->
                    SegmentedButton(
                        selected = state.filter == filter,
                        onClick = { onFilterChange(filter) },
                        shape = SegmentedButtonDefaults.itemShape(index, LocationFilter.entries.size),
                        label = {
                            Text(
                                when (filter) {
                                    LocationFilter.ALL -> "Todas"
                                    LocationFilter.FAVORITES -> "Favoritas"
                                    LocationFilter.NOT_FAVORITES -> "No favoritas"
                                },
                            )
                        },
                    )
                }
            }
            if (state.data.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No hay locations para este filtro.")
                }
            } else {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 8.dp)) {
                    items(state.data, key = { it.id }) { location ->
                        ListItem(
                            headlineContent = { Text(location.name) },
                            supportingContent = { Text("${location.type} · ${location.dimension}") },
                            trailingContent = {
                                IconButton(onClick = { onFavoriteClick(location) }) {
                                    Text(if (location.isFavorite) "★" else "☆", style = MaterialTheme.typography.headlineSmall)
                                }
                            },
                            modifier = Modifier.clickable { onLocationClick(location.id) },
                        )
                    }
                }
            }
        }
    }
}
