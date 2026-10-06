package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.sergio.lab7.R
import edu.sergio.lab7.ui.viewmodel.LocationDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsScreen(
    state: LocationDetailsUiState,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
) {
    val backLabel = stringResource(R.string.back)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.location_details)) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.semantics { contentDescription = backLabel }) {
                        Text("←", fontSize = 28.sp)
                    }
                },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingLayout(onClick = onLoadingClick)
                state.hasError -> ErrorLayout(onRetry = onRetry)
                else -> Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    val location = state.data
                    if (location == null) {
                        Text(stringResource(R.string.location_not_found))
                    } else {
                        Text(location.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        LocationDetailRow(stringResource(R.string.id_label), location.id.toString())
                        LocationDetailRow(stringResource(R.string.name), location.name)
                        LocationDetailRow(stringResource(R.string.type), location.type)
                        LocationDetailRow(stringResource(R.string.dimension), location.dimension)
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
