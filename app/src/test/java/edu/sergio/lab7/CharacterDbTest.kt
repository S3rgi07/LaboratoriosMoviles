package edu.sergio.lab7

import edu.sergio.lab7.data.CharacterDb
import edu.sergio.lab7.navigation.CharacterDetails
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class CharacterDbTest {
    private val db = CharacterDb()

    @Test fun courseDataContainsTwentyUniqueCharacters() {
        val characters = db.getAllCharacters()
        assertEquals(20, characters.size)
        assertEquals(20, characters.map { it.id }.toSet().size)
        assertTrue(characters.all { it.image.startsWith("https://") })
    }

    @Test fun detailsAreResolvedFromId() {
        val morty = db.getCharacterById(2)
        assertEquals("Morty Smith", morty.name)
        assertEquals("Human", morty.species)
        assertEquals("Alive", morty.status)
        assertEquals("Male", morty.gender)
    }

    @Test fun detailsRouteSerializesOnlyTheId() {
        val json = Json.encodeToString(CharacterDetails(2))
        assertEquals("{\"id\":2}", json)
        assertEquals(CharacterDetails(2), Json.decodeFromString<CharacterDetails>(json))
    }
}
