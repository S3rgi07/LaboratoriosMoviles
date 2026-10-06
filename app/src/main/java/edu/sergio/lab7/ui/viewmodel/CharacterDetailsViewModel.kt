package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.Character
import edu.sergio.lab7.data.CharacterDb
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterDetailsUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false,
)

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val characterId: Int = checkNotNull(savedStateHandle["id"])
    private val characterDb = CharacterDb()
    private val _uiState = MutableStateFlow(CharacterDetailsUiState())
    val uiState: StateFlow<CharacterDetailsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadJob?.cancel()
        _uiState.value = CharacterDetailsUiState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(2_000)
            _uiState.value = CharacterDetailsUiState(
                data = runCatching { characterDb.getCharacterById(characterId) }.getOrNull(),
            )
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            loadJob?.cancel()
            _uiState.value = CharacterDetailsUiState(isLoading = false, hasError = true)
        }
    }
}
