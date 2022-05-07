package com.code.pokedex.framework.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.code.pokedex.data.source.PokedexRemoteDataSource
import com.code.pokedex.framework.source.local.PokedexDatabase
import com.code.pokedex.framework.source.local.model.Pokemon
import com.code.pokedex.framework.source.local.model.RemoteKeys
import com.code.pokedex.framework.utils.Utils
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_OFFSET_INDEX = 0
private const val PAGE_LIMIT = 150

@OptIn(ExperimentalPagingApi::class)
class PokedexRemoteMediator(
    private val database: PokedexDatabase,
    private val service: PokedexRemoteDataSource
) : RemoteMediator<Int, Pokemon>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Pokemon>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_OFFSET_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val apiResponse = service.getPokemons(page, PAGE_LIMIT)
            val pokemons: MutableList<Pokemon> = mutableListOf()

            val results = apiResponse.results
            val endOfPaginationReached = results.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.pokedexDao().clearPokedex()
                }
                val prevKey = if (page == STARTING_OFFSET_INDEX) null else page - PAGE_LIMIT
                val nextKey = if (endOfPaginationReached) null else page + PAGE_LIMIT
                val keys = results.map { result ->
                    val id = Utils.getPokemonId(result.url)
                    val pokemonResponse = service.getPokemon(id)
                    val types = pokemonResponse.types.map { it.type.name }
                    //val eChainId = Utils.getPokemonId(service.getPokemonSpecies(id).evolution_chain.url)
                    //val evolutions = Utils.getEvolutions(service.getEvolutionChain(eChainId).chain)
                    val moves = pokemonResponse.moves.map { it.move.name }
                    val abilities = pokemonResponse.abilities.map { it.ability.name }
                    //val locations = service.getLocationEncounters(id).map { it.location_area.name }
                    val pokemon = Pokemon(
                        id = pokemonResponse.id,
                        name = pokemonResponse.name,
                        types = types,
                        evolution_chain = emptyList(),
                        moves = moves,
                        abilities = abilities,
                        location_area_encounters = emptyList(),
                        url = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/${
                            Utils.formatId(
                                id
                            )
                        }.png",
                        favorite = false
                    )
                    Log.e("POKE", pokemon.toString())
                    pokemons.add(pokemon)
                    RemoteKeys(id = pokemon.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.pokedexDao().insertAll(pokemons)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Pokemon>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pokemon ->
                // Get the remote keys of the last item retrieved
                database.remoteKeysDao().remoteKeysId(pokemon.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Pokemon>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pokemon ->
                // Get the remote keys of the first items retrieved
                database.remoteKeysDao().remoteKeysId(pokemon.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Pokemon>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().remoteKeysId(id)
            }
        }
    }
}
