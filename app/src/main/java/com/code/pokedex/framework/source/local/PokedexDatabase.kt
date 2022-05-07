package com.code.pokedex.framework.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.code.pokedex.framework.source.local.model.Pokemon
import com.code.pokedex.framework.source.local.model.RemoteKeys
import com.code.pokedex.framework.utils.Converters

@Database(entities = [Pokemon::class, RemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PokedexDatabase : RoomDatabase() {

    abstract fun pokedexDao(): PokedexDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}