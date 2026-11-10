package edu.sergio.lab7.data.remote

import edu.sergio.lab7.data.Character
import edu.sergio.lab7.data.Location
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class RickAndMortyApi {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getAllCharacters(): List<Character> = fetchAll(
        firstUrl = "$BASE_URL/character",
        decode = { json.decodeFromString<CharacterPage>(it) },
        next = { it.info.next },
        results = { page -> page.results.map(CharacterDto::toModel) },
    )

    suspend fun getAllLocations(): List<Location> = fetchAll(
        firstUrl = "$BASE_URL/location",
        decode = { json.decodeFromString<LocationPage>(it) },
        next = { it.info.next },
        results = { page -> page.results.map(LocationDto::toModel) },
    )

    private suspend fun <T, R> fetchAll(
        firstUrl: String,
        decode: (String) -> T,
        next: (T) -> String?,
        results: (T) -> List<R>,
    ): List<R> = withContext(Dispatchers.IO) {
        buildList {
            var url: String? = firstUrl
            while (url != null) {
                val page = decode(request(url))
                addAll(results(page))
                url = next(page)
            }
        }
    }

    private fun request(url: String): String {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 15_000
            readTimeout = 15_000
            setRequestProperty("Accept", "application/json")
        }
        return try {
            check(connection.responseCode in 200..299) {
                "HTTP ${connection.responseCode} al consultar $url"
            }
            connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    private companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api"
    }
}

@Serializable
private data class PageInfo(val next: String? = null)

@Serializable
private data class CharacterPage(val info: PageInfo, val results: List<CharacterDto>)

@Serializable
private data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
) {
    fun toModel() = Character(id, name, status, species, gender, image)
}

@Serializable
private data class LocationPage(val info: PageInfo, val results: List<LocationDto>)

@Serializable
private data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
) {
    fun toModel() = Location(id, name, type, dimension)
}
