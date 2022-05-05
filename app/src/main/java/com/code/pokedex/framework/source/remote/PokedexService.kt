package com.code.pokedex.framework.source.remote

import com.code.pokedex.framework.source.remote.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokedexService {

    @GET("pokemon")
    suspend fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 150
    ): PokedexResponse

    @GET("pokemon/{id}/")
    suspend fun getPokemon(
        @Path("id") id: Int
    ): PokemonResponse

    @GET("pokemon-species/{id}/")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): SpeciesResponse

    @GET("evolution-chain/{id}/")
    suspend fun getEvolutionChain(
        @Path("id") id: Int
    ): EvolutionsChainResponse

    @GET("pokemon/{id}/encounters")
    suspend fun getLocationEncounters(
        @Path("id") id: Int
    ): List<EncountersResponse>

    companion object {
        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        fun create(): PokedexService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PokedexService::class.java)
        }
    }
}
