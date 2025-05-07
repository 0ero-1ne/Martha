package com.zero_one.martha.utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun getFileBufferedReader(
    filepath: String,
): BufferedReader {
    val url = URL(filepath)
    val fileConnection = url.openConnection() as HttpsURLConnection
    return BufferedReader(InputStreamReader(fileConnection.inputStream))
}
