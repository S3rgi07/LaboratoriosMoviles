package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.Character
import edu.sergio.lab7.data.CharacterRepository
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
    private val repository: CharacterRepository,
) : ViewModel() {
    private val characterId: Int = checkNotNull(savedStateHandle["id"])

    val uiState = repository.observeById(characterId)
        .map { CharacterDetailsUiState(isLoading = false, data = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CharacterDetailsUiState(),
        )

    fun updateCharacter(name: String, status: String, species: String, gender: String, image: String) {
        val character = uiState.value.data ?: return
        viewModelScope.launch {
            repository.update(
                character.copy(
                    name = name.trim(),
                    status = status.trim(),
                    species = species.trim(),
                    gender = gender.trim(),
                    image = image.trim(),
                ),
            )
        }
    }

    fun deleteCharacter(onDeleted: () -> Unit) {
        val character = uiState.value.data ?: return
        viewModelScope.launch {
            repository.delete(character.id)
            onDeleted()
        }
    }

    companion object {
        fun factory(repository: CharacterRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { CharacterDetailsViewModel(createSavedStateHandle(), repository) }
        }
    }
}
