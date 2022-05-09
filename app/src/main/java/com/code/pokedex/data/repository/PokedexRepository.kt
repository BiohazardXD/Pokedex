package com.code.pokedex.data.repository

import androidx.paging.PagingData
import com.code.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {
    suspend fun getPokemons(): Flow<PagingData<Pokemon>>
    suspend fun searchPokemon(query: String):  Flow<PagingData<Pokemon>>
}