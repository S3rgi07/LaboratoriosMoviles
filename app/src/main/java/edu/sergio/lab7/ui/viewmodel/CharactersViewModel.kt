package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.Character
import edu.sergio.lab7.data.CharacterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
)

class CharactersViewModel(private val repository: CharacterRepository) : ViewModel() {
    val uiState = repository.observeAll().map { characters ->
        CharactersUiState(isLoading = false, data = characters)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CharactersUiState(),
    )

    fun createCharacter(name: String, status: String, species: String, gender: String, image: String) {
        viewModelScope.launch {
            repository.create(
                Character(
                    id = repository.nextId(),
                    name = name.trim(),
                    status = status.trim(),
                    species = species.trim(),
                    gender = gender.trim(),
                    image = image.trim(),
                ),
            )
        }
    }

    companion object {
        fun factory(repository: CharacterRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { CharactersViewModel(repository) }
        }
    }
}
