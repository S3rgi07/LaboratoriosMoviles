package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.Location
import edu.sergio.lab7.data.local.LocationDao
import edu.sergio.lab7.data.local.toModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LocationDetailsUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false,
)

class LocationDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    locationDao: LocationDao,
) : ViewModel() {
    private val locationId: Int = checkNotNull(savedStateHandle["id"])

    val uiState = locationDao.observeById(locationId)
        .map { LocationDetailsUiState(isLoading = false, data = it?.toModel()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocationDetailsUiState(),
        )

    companion object {
        fun factory(dao: LocationDao): ViewModelProvider.Factory = viewModelFactory {
            initializer { LocationDetailsViewModel(createSavedStateHandle(), dao) }
        }
    }
}
