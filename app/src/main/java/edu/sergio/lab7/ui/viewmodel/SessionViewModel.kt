package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.SessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SessionUiState {
    data object Loading : SessionUiState
    data object LoggedOut : SessionUiState
    data class LoggedIn(val userName: String) : SessionUiState
}

class SessionViewModel(private val repository: SessionRepository) : ViewModel() {
    val uiState = repository.userName
        .map { name ->
            if (name == null) SessionUiState.LoggedOut else SessionUiState.LoggedIn(name)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SessionUiState.Loading,
        )

    fun logIn(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.saveUserName(name) }
    }

    fun logOut() {
        viewModelScope.launch { repository.clearUserName() }
    }

    companion object {
        fun factory(repository: SessionRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { SessionViewModel(repository) }
        }
    }
}
