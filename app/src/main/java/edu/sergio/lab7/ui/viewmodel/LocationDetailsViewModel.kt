package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.Location
import edu.sergio.lab7.data.LocationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LocationDetailsUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false,
)

class LocationDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: LocationRepository,
) : ViewModel() {
    private val locationId: Int = checkNotNull(savedStateHandle["id"])

    val uiState = repository.observeById(locationId)
        .map { LocationDetailsUiState(isLoading = false, data = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocationDetailsUiState(),
        )

    fun toggleFavorite() {
        val location = uiState.value.data ?: return
        viewModelScope.launch {
            repository.setFavorite(location.id, !location.isFavorite)
        }
    }

    companion object {
        fun factory(repository: LocationRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { LocationDetailsViewModel(createSavedStateHandle(), repository) }
        }
    }
}
