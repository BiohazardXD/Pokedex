package com.code.pokedex.framework.source.remote.model

import com.google.gson.annotations.SerializedName

data class PokedexResponse(
    @SerializedName("count") val count: Int = 0,
    @SerializedName("next") val next: String,
    @SerializedName("previous") val previous: String,
    @SerializedName("results") val results: List<Results> = emptyList()
)