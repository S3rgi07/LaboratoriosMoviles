package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.sergio.lab7.ui.viewmodel.LocationsUiState

@Composable
fun LocationsScreen(
    state: LocationsUiState,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit,
    onLocationClick: (Int) -> Unit,
) {
    when {
        state.isLoading -> LoadingLayout(onClick = onLoadingClick)
        state.hasError -> ErrorLayout(onRetry = onRetry)
        else -> LocationsList(state.data, onLocationClick)
    }
}

@Composable
private fun LocationsList(
    locations: List<edu.sergio.lab7.data.Location>,
    onLocationClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(locations, key = { it.id }) { location ->
            ListItem(
                headlineContent = { Text(location.name) },
                supportingContent = { Text(location.type) },
                modifier = Modifier.clickable { onLocationClick(location.id) },
            )
        }
    }
}
