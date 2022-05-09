package com.code.pokedex.framework.repository

import com.code.pokedex.data.repository.PokedexRepository
import com.code.pokedex.data.source.PokedexRemoteDataSource
import com.code.pokedex.framework.source.local.PokedexDatabase
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PokedexRepositoryImplTest {

    @Mock
    lateinit var pokedexLocalDataSource: PokedexDatabase
    @Mock
    lateinit var pokedexRemoteDataSource: PokedexRemoteDataSource

    lateinit var pokedexRepository: PokedexRepository

    @Before
    fun setUp() {
        pokedexRepository = PokedexRepositoryImpl(pokedexLocalDataSource, pokedexRemoteDataSource)
    }


}