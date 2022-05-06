package com.code.pokedex.framework.source.local.utils

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        val listType = object :
            TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromInteger(value: Int?): List<Int?> {
        return listOf(value)
    }

    @TypeConverter
    fun fromList(list: List<Int?>?): Int {
        val gson = Gson()
        Log.e("CONV", gson.toJson(list))
        return gson.toJson(list).length
    }
}