package edu.sergio.lab7.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Login

@Serializable
data object Characters

// La única información que se envía al detalle es el ID.
@Serializable
data class CharacterDetails(val id: Int)
