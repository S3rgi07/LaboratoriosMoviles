package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.Location
import edu.sergio.lab7.data.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class LocationFilter { ALL, FAVORITES, NOT_FAVORITES }

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false,
    val filter: LocationFilter = LocationFilter.ALL,
)

class LocationsViewModel(private val repository: LocationRepository) : ViewModel() {
    private val filter = MutableStateFlow(LocationFilter.ALL)

    val uiState = combine(repository.observeAll(), filter) { locations, selectedFilter ->
        LocationsUiState(
            isLoading = false,
            data = when (selectedFilter) {
                LocationFilter.ALL -> locations
                LocationFilter.FAVORITES -> locations.filter(Location::isFavorite)
                LocationFilter.NOT_FAVORITES -> locations.filterNot(Location::isFavorite)
            },
            filter = selectedFilter,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocationsUiState(),
        )

    fun setFilter(value: LocationFilter) {
        filter.value = value
    }

    fun toggleFavorite(location: Location) {
        viewModelScope.launch {
            repository.setFavorite(location.id, !location.isFavorite)
        }
    }

    companion object {
        fun factory(repository: LocationRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { LocationsViewModel(repository) }
        }
    }
}
