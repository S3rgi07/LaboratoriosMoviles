package edu.sergio.lab7.ui.viewmodel

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

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
)

class CharactersViewModel(
    private val characterDb: CharacterDb = CharacterDb(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadJob?.cancel()
        _uiState.value = CharactersUiState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(4_000)
            _uiState.value = CharactersUiState(data = characterDb.getAllCharacters())
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            loadJob?.cancel()
            _uiState.value = CharactersUiState(isLoading = false, hasError = true)
        }
    }
}
