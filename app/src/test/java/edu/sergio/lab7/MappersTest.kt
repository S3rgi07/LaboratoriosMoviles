package edu.sergio.lab7

import edu.sergio.lab7.data.Character
import edu.sergio.lab7.data.Location
import edu.sergio.lab7.data.local.toEntity
import edu.sergio.lab7.data.local.toModel
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {
    @Test
    fun characterRoundTripPreservesFavoriteState() {
        val character = Character(1, "Rick", "Alive", "Human", "Male", "image", true)

        assertEquals(character, character.toEntity().toModel())
    }

    @Test
    fun locationRoundTripPreservesAllFields() {
        val location = Location(1, "Earth", "Planet", "C-137")

        assertEquals(location, location.toEntity().toModel())
    }
}
