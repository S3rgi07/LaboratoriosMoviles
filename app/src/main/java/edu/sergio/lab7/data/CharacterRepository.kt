package edu.sergio.lab7.data

import edu.sergio.lab7.data.local.CharacterDao
import edu.sergio.lab7.data.local.toEntity
import edu.sergio.lab7.data.local.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CharacterRepository {
    fun observeAll(): Flow<List<Character>>
    fun observeById(id: Int): Flow<Character?>
    suspend fun create(character: Character)
    suspend fun update(character: Character)
    suspend fun delete(id: Int)
    suspend fun nextId(): Int
}

class LocalCharacterRepository(private val dao: CharacterDao) : CharacterRepository {
    override fun observeAll() = dao.observeAll().map { entities -> entities.map { it.toModel() } }
    override fun observeById(id: Int) = dao.observeById(id).map { it?.toModel() }
    override suspend fun create(character: Character) = dao.insert(character.toEntity())
    override suspend fun update(character: Character) = dao.update(character.toEntity())
    override suspend fun delete(id: Int) = dao.deleteById(id)
    override suspend fun nextId() = dao.maxId() + 1
}
