package com.code.pokedex.framework.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.code.pokedex.framework.source.local.model.Pokemon

@Dao
interface PokedexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Pokemon>)

    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getAll(): PagingSource<Int, Pokemon>

    @Query(
        "SELECT * FROM pokemon WHERE " +
                "name LIKE '%' || :query || '%' " +
                "ORDER BY id ASC"
    )
    fun searchByName(query: String): PagingSource<Int, Pokemon>

    @Query("SELECT * FROM pokemon WHERE favorite = true ORDER BY id ASC")
    fun allFavorites(): PagingSource<Int, Pokemon>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun findById(id: Int): Pokemon

    @Query("DELETE FROM pokemon")
    suspend fun clearPokedex()
}