package com.code.pokedex.framework.di

import com.code.pokedex.data.source.PokedexRemoteDataSource
import com.code.pokedex.framework.repository.PokedexRepositoryImpl
import com.code.pokedex.framework.source.local.PokedexDatabase
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Reusable
    fun pokedexRepositoryProvides(database: PokedexDatabase, service: PokedexRemoteDataSource) =
        PokedexRepositoryImpl(database, service)
}