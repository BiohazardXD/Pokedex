package com.code.pokedex.domain.usescase

import androidx.paging.PagingData
import com.code.pokedex.data.repository.PokedexRepository
import com.code.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface GetAllPokemons {
    suspend fun execute(): Flow<PagingData<Pokemon>>
}

class GetAllPokemonsImpl(
    private val pokedexRepository: PokedexRepository
): GetAllPokemons {
    override suspend fun execute(): Flow<PagingData<Pokemon>> = pokedexRepository.getPokemons()
}