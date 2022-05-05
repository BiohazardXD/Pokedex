package com.code.pokedex.framework.source.remote.model

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)