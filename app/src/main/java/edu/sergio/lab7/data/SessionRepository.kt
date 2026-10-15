package edu.sergio.lab7.data

import android.content.Context
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore(name = "session")

interface SessionRepository {
    val userName: Flow<String?>

    suspend fun saveUserName(name: String)

    suspend fun clearUserName()
}

class DataStoreSessionRepository(private val context: Context) : SessionRepository {
    override val userName: Flow<String?> = context.sessionDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[USER_NAME]?.trim()?.takeIf(String::isNotEmpty)
        }

    override suspend fun saveUserName(name: String) {
        val normalizedName = name.trim()
        require(normalizedName.isNotEmpty()) { "El nombre no puede estar vacío" }
        context.sessionDataStore.edit { preferences ->
            preferences[USER_NAME] = normalizedName
        }
    }

    override suspend fun clearUserName() {
        context.sessionDataStore.edit { preferences ->
            preferences.remove(USER_NAME)
        }
    }

    private companion object {
        val USER_NAME = stringPreferencesKey("name")
    }
}
