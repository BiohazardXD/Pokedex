package com.code.pokedex.utils

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.MockResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets.UTF_8


fun MockResponse.fromJson(jsonFile: String): MockResponse =
    setBody(readJsonFile(jsonFile))

private
fun readJsonFile(jsonFilePath: String): String {
    val context = InstrumentationRegistry.getInstrumentation().context

    try {
        val inputStream = context.assets.open(jsonFilePath)
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach {
            builder.append(it)
        }
        return builder.toString()
    } catch (e: IOException) {
        throw e
    }
}