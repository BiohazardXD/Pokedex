package com.code.pokedex.framework.utils

import com.code.pokedex.domain.model.Pokemon
import com.code.pokedex.framework.source.local.model.Pokemon as PokemonEntity

fun Pokemon.toRoomEntity(): PokemonEntity = PokemonEntity(
    id, name, types, evolution_chain, moves, abilities, location_area_encounters, url, favorite
)

fun PokemonEntity.toDomainEntity(): Pokemon = Pokemon(
    id, name, types, evolution_chain, moves, abilities, location_area_encounters, url, favorite
)