package com.code.pokedex.domain.usescase

import androidx.paging.PagingData
import com.code.pokedex.framework.repository.PokedexRepositoryImpl
import com.code.pokedex.utils.mokedPokemon
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchPokemonByIdImplTest {

    @Mock
    lateinit var pokedexRepository: PokedexRepositoryImpl
    lateinit var searchPokemonById: SearchPokemonByIdImpl

    @Before
    fun setUp() {
        searchPokemonById = SearchPokemonByIdImpl(pokedexRepository)
    }

    @Test
    fun `ejecuta llamadas al repositorio`() {
        runBlocking {
            val pokemons = flow {
                emit(PagingData.from(listOf(mokedPokemon.copy(1))))
            }
            val id = 1
            whenever(pokedexRepository.searchPokemonById(id)).thenReturn(pokemons)

            val result = searchPokemonById.execute(id)

            assertEquals(pokemons, result)
        }
    }
}