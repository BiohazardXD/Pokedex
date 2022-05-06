package com.code.pokedex.data.source

import com.code.pokedex.framework.source.remote.model.*

interface PokedexRemoteDataSource {

    suspend fun getPokemons(offset: Int, limit: Int = 150): PokedexResponse

    suspend fun getPokemon(id: Int): PokemonResponse

    suspend fun getPokemonSpecies(id: Int): SpeciesResponse

    suspend fun getEvolutionChain(id: Int): EvolutionsChainResponse

    suspend fun getLocationEncounters(id: Int): List<EncountersResponse>
}