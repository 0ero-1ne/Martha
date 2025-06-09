package com.zero_one.martha.utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun getFileBufferedReader(
    filepath: String,
): BufferedReader {
    val url = URL(filepath)
    val fileConnection = url.openConnection() as HttpURLConnection
    return BufferedReader(InputStreamReader(fileConnection.inputStream))
}
