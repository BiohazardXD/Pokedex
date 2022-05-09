package com.code.pokedex.domain.usescase

import androidx.paging.PagingData
import com.code.pokedex.data.repository.PokedexRepository
import com.code.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface SearchPokemon {
    suspend fun execute(query: String): Flow<PagingData<Pokemon>>
}
class SearchPokemonImpl(private val pokedexRepository: PokedexRepository): SearchPokemon {
    override suspend fun execute(query: String): Flow<PagingData<Pokemon>> = pokedexRepository.searchPokemon(query)
}