package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.Character
import edu.sergio.lab7.data.local.CharacterDao
import edu.sergio.lab7.data.local.toModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val showFavoritesOnly: Boolean = false,
)

class CharactersViewModel(private val characterDao: CharacterDao) : ViewModel() {
    private val showFavoritesOnly = MutableStateFlow(false)

    val uiState = combine(characterDao.observeAll(), showFavoritesOnly) { entities, onlyFavorites ->
        val characters = entities.map { it.toModel() }
        CharactersUiState(
            isLoading = false,
            data = if (onlyFavorites) characters.filter(Character::isFavorite) else characters,
            showFavoritesOnly = onlyFavorites,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CharactersUiState(),
    )

    fun setFavoritesOnly(enabled: Boolean) {
        showFavoritesOnly.value = enabled
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch { characterDao.updateFavorite(character.id, !character.isFavorite) }
    }

    companion object {
        fun factory(dao: CharacterDao): ViewModelProvider.Factory = viewModelFactory {
            initializer { CharactersViewModel(dao) }
        }
    }
}
