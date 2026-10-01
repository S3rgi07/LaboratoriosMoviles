package edu.sergio.lab7.navigation

import kotlinx.serialization.Serializable

@Serializable data object Login

// Root destinations used by the bottom navigation.
@Serializable data object CharactersGraph
@Serializable data object LocationsGraph
@Serializable data object ProfileGraph

@Serializable data object Characters
@Serializable data class CharacterDetails(val id: Int)

@Serializable data object Locations
// Only the location ID is passed to the details destination.
@Serializable data class LocationDetails(val id: Int)

@Serializable data object Profile
