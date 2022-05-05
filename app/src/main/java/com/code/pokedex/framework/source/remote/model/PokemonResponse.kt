package com.code.pokedex.framework.source.remote.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("types") val types: List<Types>,
    @SerializedName("moves") val moves: List<Moves>,
    @SerializedName("abilities") val abilities: List<Abilities>,
    @SerializedName("location_area_encounters") val location_area_encounters: String,
    @SerializedName("species") val species: Species
)

data class Types(
    @SerializedName("type") val type: Type
)

data class Type(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class SpeciesResponse(
    @SerializedName("evolution_chain") val evolution_chain: EvolutionChain
)

data class EvolutionChain(
    @SerializedName("url") val url: String
)

data class EvolutionsChainResponse(
    @SerializedName("chain") val chain: Chain
)

data class Chain(
    @SerializedName("evolves_to") val evolves_to: List<Chain>,
    @SerializedName("species") val species: Species
)

data class Moves(
    @SerializedName("move") val move: Move,
)

data class Move(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class Abilities(
    @SerializedName("ability") val ability: Ability
)

data class Ability(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class EncountersResponse(
    @SerializedName("location_area") val location_area: Location
)

data class Location(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class Species(
    @SerializedName("url") val url: String
)