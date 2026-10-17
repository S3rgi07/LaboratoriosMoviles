package edu.sergio.lab7.data.local

import edu.sergio.lab7.data.Character
import edu.sergio.lab7.data.Location

fun CharacterEntity.toModel() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite,
)

fun Character.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite,
)

fun LocationEntity.toModel() = Location(id = id, name = name, type = type, dimension = dimension)

fun Location.toEntity() = LocationEntity(id = id, name = name, type = type, dimension = dimension)
