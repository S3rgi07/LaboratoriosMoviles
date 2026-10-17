package edu.sergio.lab7.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import edu.sergio.lab7.data.InitialSyncRepository
import edu.sergio.lab7.data.SessionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SessionUiState {
    data object Loading : SessionUiState
    data object LoggedOut : SessionUiState
    data class LoggedIn(val userName: String) : SessionUiState
}

data class LoginUiState(
    val isSynchronizing: Boolean = false,
    val hasError: Boolean = false,
)

class SessionViewModel(
    private val repository: SessionRepository,
    private val syncRepository: InitialSyncRepository,
) : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

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
        if (name.isBlank() || _loginUiState.value.isSynchronizing) return
        viewModelScope.launch {
            _loginUiState.value = LoginUiState(isSynchronizing = true)
            runCatching {
                delay(4_000)
                syncRepository.synchronizeIfNeeded()
                repository.saveUserName(name)
                _loginUiState.value = LoginUiState()
            }.onFailure {
                _loginUiState.value = LoginUiState(hasError = true)
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            syncRepository.clearAll()
            repository.clearUserName()
        }
    }

    companion object {
        fun factory(
            repository: SessionRepository,
            syncRepository: InitialSyncRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer { SessionViewModel(repository, syncRepository) }
        }
    }
}
