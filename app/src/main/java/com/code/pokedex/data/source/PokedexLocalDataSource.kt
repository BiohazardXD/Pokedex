package com.code.pokedex.data.source

import androidx.paging.PagingSource
import com.code.pokedex.framework.source.local.model.Pokemon
import com.code.pokedex.framework.source.local.model.RemoteKeys

interface PokedexLocalDataSource {

    suspend fun savePokemons(list: List<Pokemon>)

    fun getAllPokemons(): PagingSource<Int, Pokemon>

    fun searchByName(query: String): PagingSource<Int, Pokemon>

    fun allFavorites(): PagingSource<Int, Pokemon>

    fun findById(id: Int): Pokemon

    suspend fun clearPokedex()

    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    suspend fun remoteKeysId(id: Int): RemoteKeys?

    suspend fun clearRemoteKeys()
}