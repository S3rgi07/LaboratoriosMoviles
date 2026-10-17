package edu.sergio.lab7.data

import androidx.room.withTransaction
import edu.sergio.lab7.data.local.AppDatabase
import edu.sergio.lab7.data.local.toEntity

class InitialSyncRepository(private val database: AppDatabase) {
    suspend fun synchronizeIfNeeded() = database.withTransaction {
        if (database.characterDao().count() == 0) {
            database.characterDao().insertAll(CharacterDb().getAllCharacters().map { it.toEntity() })
        }
        if (database.locationDao().count() == 0) {
            database.locationDao().insertAll(LocationDb().getAllLocations().map { it.toEntity() })
        }
    }

    suspend fun clearAll() = database.withTransaction {
        database.characterDao().deleteAll()
        database.locationDao().deleteAll()
    }
}
