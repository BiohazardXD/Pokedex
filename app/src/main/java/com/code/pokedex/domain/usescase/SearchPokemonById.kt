package com.code.pokedex.domain.usescase

import androidx.paging.PagingData
import com.code.pokedex.data.repository.PokedexRepository
import com.code.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface SearchPokemonById {

    suspend fun execute(id: Int): Flow<PagingData<Pokemon>>
}

class SearchPokemonByIdImpl(
    private val pokedexRepository: PokedexRepository
) : SearchPokemonById {

    override suspend fun execute(id: Int): Flow<PagingData<Pokemon>> = pokedexRepository.searchPokemonById(id)
}