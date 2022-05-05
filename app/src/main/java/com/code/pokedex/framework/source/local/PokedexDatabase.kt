package com.code.pokedex.framework.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.code.pokedex.framework.source.local.model.Pokemon
import com.code.pokedex.framework.source.local.model.RemoteKeys
import com.code.pokedex.framework.source.local.utils.Converters

@Database(entities = [Pokemon::class, RemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PokedexDatabase : RoomDatabase() {

    abstract fun pokedexDao(): PokedexDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    @Volatile
    private var INSTANCE: PokedexDatabase? = null

    fun getInstance(context: Context): PokedexDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

    fun buildDatabase(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            PokedexDatabase::class.java,
            "Pokedex.db"
        ).build()
}