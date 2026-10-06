package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.Location
import edu.sergio.lab7.data.LocationDb
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false,
)

class LocationsViewModel(
    private val locationDb: LocationDb = LocationDb(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(LocationsUiState())
    val uiState: StateFlow<LocationsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadJob?.cancel()
        _uiState.value = LocationsUiState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(4_000)
            _uiState.value = LocationsUiState(data = locationDb.getAllLocations())
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            loadJob?.cancel()
            _uiState.value = LocationsUiState(isLoading = false, hasError = true)
        }
    }
}
