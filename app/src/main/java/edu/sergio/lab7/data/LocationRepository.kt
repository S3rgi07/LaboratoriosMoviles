package edu.sergio.lab7.data

import edu.sergio.lab7.data.local.LocationDao
import edu.sergio.lab7.data.local.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LocationRepository {
    fun observeAll(): Flow<List<Location>>
    fun observeById(id: Int): Flow<Location?>
    suspend fun setFavorite(id: Int, favorite: Boolean)
}

class LocalLocationRepository(private val dao: LocationDao) : LocationRepository {
    override fun observeAll() = dao.observeAll().map { entities -> entities.map { it.toModel() } }
    override fun observeById(id: Int) = dao.observeById(id).map { it?.toModel() }
    override suspend fun setFavorite(id: Int, favorite: Boolean) = dao.updateFavorite(id, favorite)
}
