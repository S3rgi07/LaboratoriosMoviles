package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
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

data class LocationDetailsUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false,
)

class LocationDetailsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val locationId: Int = checkNotNull(savedStateHandle["id"])
    private val locationDb = LocationDb()
    private val _uiState = MutableStateFlow(LocationDetailsUiState())
    val uiState: StateFlow<LocationDetailsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadJob?.cancel()
        _uiState.value = LocationDetailsUiState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(2_000)
            _uiState.value = LocationDetailsUiState(
                data = runCatching { locationDb.getLocationById(locationId) }.getOrNull(),
            )
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            loadJob?.cancel()
            _uiState.value = LocationDetailsUiState(isLoading = false, hasError = true)
        }
    }
}
