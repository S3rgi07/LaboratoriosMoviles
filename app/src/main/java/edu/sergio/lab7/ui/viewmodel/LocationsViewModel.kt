package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.Location
import edu.sergio.lab7.data.local.LocationDao
import edu.sergio.lab7.data.local.toModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false,
)

class LocationsViewModel(locationDao: LocationDao) : ViewModel() {
    val uiState = locationDao.observeAll()
        .map { entities -> LocationsUiState(isLoading = false, data = entities.map { it.toModel() }) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocationsUiState(),
        )

    companion object {
        fun factory(dao: LocationDao): ViewModelProvider.Factory = viewModelFactory {
            initializer { LocationsViewModel(dao) }
        }
    }
}
