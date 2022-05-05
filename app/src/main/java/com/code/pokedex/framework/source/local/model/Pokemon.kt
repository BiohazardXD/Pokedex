package com.code.pokedex.framework.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pokemon")
data class Pokemon(
    @PrimaryKey
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("types") val types: List<String>,
    @field:SerializedName("evolution_chain") val evolution_chain: List<Int>,
    @field:SerializedName("moves") val moves: List<String>,
    @field:SerializedName("abilities") val abilities: List<String>,
    @field:SerializedName("location_area_encounters") val location_area_encounters: List<String>,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("favorite") val favorite: Boolean
)
