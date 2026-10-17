package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.Character
import edu.sergio.lab7.data.local.CharacterDao
import edu.sergio.lab7.data.local.toModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CharacterDetailsUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false,
)

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val characterDao: CharacterDao,
) : ViewModel() {
    private val characterId: Int = checkNotNull(savedStateHandle["id"])

    val uiState = characterDao.observeById(characterId)
        .map { CharacterDetailsUiState(isLoading = false, data = it?.toModel()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CharacterDetailsUiState(),
        )

    fun toggleFavorite() {
        val character = uiState.value.data ?: return
        viewModelScope.launch { characterDao.updateFavorite(character.id, !character.isFavorite) }
    }

    companion object {
        fun factory(dao: CharacterDao): ViewModelProvider.Factory = viewModelFactory {
            initializer { CharacterDetailsViewModel(createSavedStateHandle(), dao) }
        }
    }
}
