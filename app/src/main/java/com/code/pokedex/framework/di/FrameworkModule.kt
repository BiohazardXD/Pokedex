package com.code.pokedex.framework.di

import android.app.Application
import androidx.room.Room
import com.code.pokedex.data.source.PokedexLocalDataSource
import com.code.pokedex.data.source.PokedexRemoteDataSource
import com.code.pokedex.framework.source.local.PokedexDatabase
import com.code.pokedex.framework.source.local.PokedexLocalDataSourceImpl
import com.code.pokedex.framework.source.remote.PokedexRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "Pokedex.db"

@Module
@InstallIn(SingletonComponent::class)
class FrameworkModule {

    @Provides
    @Singleton
    fun databaseProvider(application: Application) = Room.databaseBuilder(
        application,
        PokedexDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun pokedexLocalDataSource(database: PokedexDatabase): PokedexLocalDataSource =
        PokedexLocalDataSourceImpl(database)

    @Provides
    fun pokedexRemoteDataSource(): PokedexRemoteDataSource =
        PokedexRemoteDataSourceImpl()
}