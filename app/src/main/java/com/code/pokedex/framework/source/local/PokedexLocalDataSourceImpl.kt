package com.code.pokedex.framework.source.local

import androidx.paging.PagingSource
import com.code.pokedex.data.source.PokedexLocalDataSource
import com.code.pokedex.framework.source.local.model.Pokemon
import com.code.pokedex.framework.source.local.model.RemoteKeys

class PokedexLocalDataSourceImpl(database: PokedexDatabase): PokedexLocalDataSource {

    private val pokedexDao = database.pokedexDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun savePokemons(list: List<Pokemon>) {
        pokedexDao.insertAll(list)
    }

    override fun getAllPokemons(): PagingSource<Int, Pokemon> {
        return pokedexDao.getAll()
    }

    override fun searchByName(query: String): PagingSource<Int, Pokemon> {
        return pokedexDao.searchByName(query)
    }

    override fun allFavorites(): PagingSource<Int, Pokemon> {
        return pokedexDao.allFavorites()
    }

    override fun findById(id: Int): PagingSource<Int,Pokemon> {
        return pokedexDao.findById(id)
    }

    override suspend fun clearPokedex() {
        pokedexDao.clearPokedex()
    }

    override suspend fun insertAll(remoteKey: List<RemoteKeys>) {
        remoteKeysDao.insertAll(remoteKey)
    }

    override suspend fun remoteKeysId(id: Int): RemoteKeys? {
        return remoteKeysDao.remoteKeysId(id)
    }

    override suspend fun clearRemoteKeys() {
        remoteKeysDao.clearRemoteKeys()
    }
}