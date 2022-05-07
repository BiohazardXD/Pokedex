package com.code.pokedex.framework.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

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
        return gson.toJson(list).length
    }

    @TypeConverter
    fun bitMapToString(bitmap: Bitmap): String? {
        val baos  = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos)
        val b = baos.toByteArray()
        val temp = Base64.encodeToString(b, Base64.DEFAULT)
        return temp
    }

    @TypeConverter
    fun stringToBitMap(encodedString: String): Bitmap? {
        return try {
            val encodeByte = Base64.decode(encodedString,Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            bitmap

        } catch(e: Exception) {
            e.message
            null
        }
    }
}