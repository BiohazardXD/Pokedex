package com.code.pokedex.framework.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    @field:SerializedName("id") val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
