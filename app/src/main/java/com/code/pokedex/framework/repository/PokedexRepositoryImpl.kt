package com.code.pokedex.framework.repository

import androidx.paging.*
import com.code.pokedex.data.repository.PokedexRepository
import com.code.pokedex.data.source.PokedexRemoteDataSource
import com.code.pokedex.domain.model.Pokemon
import com.code.pokedex.framework.source.local.PokedexDatabase
import com.code.pokedex.framework.utils.toDomainEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokedexRepositoryImpl @Inject constructor(
    private val database: PokedexDatabase,
    private val service: PokedexRemoteDataSource,
): PokedexRepository {

    override suspend fun getPokemons(): Flow<PagingData<Pokemon>> {
        val pagingSourceFactory = { database.pokedexDao().getAll() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PokedexRemoteMediator(
                database,
                service
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { data -> data.map { it.toDomainEntity() } }
    }

    override suspend fun searchPokemon(query: String): Flow<PagingData<Pokemon>> {
        val pagingSourceFactory = { database.pokedexDao().searchByName(query) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PokedexRemoteMediator(
                database,
                service
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { data -> data.map { it.toDomainEntity() } }
    }

    override suspend fun searchPokemonById(id: Int): Flow<PagingData<Pokemon>> {
        val pagingSourceFactory = { database.pokedexDao().findById(id) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PokedexRemoteMediator(
                database,
                service
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { data -> data.map { it.toDomainEntity() } }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 150
    }
}