package com.zero_one.martha.features.main.book

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class BookRoute(
    val bookId: UInt
)

val UIntNavType = object: NavType<UInt>(
    isNullableAllowed = false,
) {
    override fun get(bundle: Bundle, key: String): UInt {
        return Json.decodeFromString(bundle.getString(key)!!)
    }

    override fun parseValue(value: String): UInt {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: UInt): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: UInt) {
        bundle.putString(key, Json.encodeToString(value))
    }

}
