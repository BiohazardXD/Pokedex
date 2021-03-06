package com.code.pokedex.presentation.di

import androidx.lifecycle.SavedStateHandle
import com.code.pokedex.domain.usescase.GetAllPokemonsImpl
import com.code.pokedex.domain.usescase.SearchPokemonById
import com.code.pokedex.domain.usescase.SearchPokemonByIdImpl
import com.code.pokedex.domain.usescase.SearchPokemonImpl
import com.code.pokedex.framework.repository.PokedexRepositoryImpl
import com.code.pokedex.presentation.viewmodel.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HomeFragmentModule {

    @Provides
    fun getAllPokemonsUseCaseProvider(
        getAllPokemons: GetAllPokemonsImpl,
        searchPokemon: SearchPokemonImpl,
        searchPokemonById: SearchPokemonByIdImpl
    ) = HomeViewModel(getAllPokemons, searchPokemon, searchPokemonById, SavedStateHandle())

    @Provides
    fun getAllPokemonsProvider(pokedexRepository: PokedexRepositoryImpl) =
        GetAllPokemonsImpl(pokedexRepository)

    @Provides
    fun searchPokemonProvider(pokedexRepository: PokedexRepositoryImpl) =
        SearchPokemonImpl(pokedexRepository)

    @Provides
    fun searchPokemonByIdProvider(pokedexRepository: PokedexRepositoryImpl) =
        SearchPokemonByIdImpl(pokedexRepository)
}