package com.code.pokedex.framework.source.local.utils

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
    fun fromInteger(value: Int?): List<Int> {
        val listType = object :
            TypeToken<ArrayList<Int?>?>() {}.type
        return Gson().fromJson(value.toString(), listType)
    }

    @TypeConverter
    fun fromList2(list: List<Int?>?): Int {
        val gson = Gson()
        return gson.toJson(list).toInt()
    }
}