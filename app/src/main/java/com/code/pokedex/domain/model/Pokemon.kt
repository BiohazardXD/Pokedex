package com.code.pokedex.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<String>,
    val evolution_chain: List<Int>,
    val moves: List<String>,
    val abilities: List<String>,
    val location_area_encounters: List<String>,
    val url: String,
    val favorite: Boolean
)
