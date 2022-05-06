package com.code.pokedex.framework.source.remote

import com.code.pokedex.data.source.PokedexRemoteDataSource
import com.code.pokedex.framework.source.remote.model.*

class PokedexRemoteDataSourceImpl(): PokedexRemoteDataSource {

    override suspend fun getPokemons(offset: Int, limit: Int): PokedexResponse {
        return RetrofitService.service.getPokemons(offset, limit)
    }

    override suspend fun getPokemon(id: Int): PokemonResponse {
        return RetrofitService.service.getPokemon(id)
    }

    override suspend fun getPokemonSpecies(id: Int): SpeciesResponse {
        return RetrofitService.service.getPokemonSpecies(id)
    }

    override suspend fun getEvolutionChain(id: Int): EvolutionsChainResponse {
        return RetrofitService.service.getEvolutionChain(id)
    }

    override suspend fun getLocationEncounters(id: Int): List<EncountersResponse> {
        return RetrofitService.service.getLocationEncounters(id)
    }
}