package com.code.pokedex.presentation.di

import com.code.pokedex.data.repository.PokedexRepository
import com.code.pokedex.domain.usescase.GetAllPokemonsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HomeFragmentModule {

    @Provides
    fun getAllPokemonsProvider(pokedexRepository: PokedexRepository) = GetAllPokemonsImpl(pokedexRepository)
}