package edu.sergio.lab7.data

import androidx.room.withTransaction
import edu.sergio.lab7.data.local.AppDatabase
import edu.sergio.lab7.data.local.toEntity
import edu.sergio.lab7.data.remote.RickAndMortyApi

class InitialSyncRepository(
    private val database: AppDatabase,
    private val api: RickAndMortyApi = RickAndMortyApi(),
) {
    suspend fun synchronizeIfNeeded() {
        if (database.characterDao().count() > 0 && database.locationDao().count() > 0) return

        val characters = api.getAllCharacters()
        val locations = api.getAllLocations()
        database.withTransaction {
            database.characterDao().deleteAll()
            database.locationDao().deleteAll()
            database.characterDao().insertAll(characters.map { it.toEntity() })
            database.locationDao().insertAll(locations.map { it.toEntity() })
        }
    }

    suspend fun clearAll() = database.withTransaction {
        database.characterDao().deleteAll()
        database.locationDao().deleteAll()
    }
}
