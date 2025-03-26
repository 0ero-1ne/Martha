package com.zero_one.martha.data.source.datastore.user

import androidx.datastore.core.Serializer
import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object TokensSerializer : Serializer<AuthTokens> {
    override val defaultValue: AuthTokens
        get() = AuthTokens("", "")

    override suspend fun readFrom(input: InputStream): AuthTokens {
        return try {
            Json.decodeFromString(
                deserializer = AuthTokens.serializer(),
                string = input.readBytes().toString(),
            )
        } catch (error: SerializationException) {
            error.printStackTrace()
            AuthTokens("", "")
        }
    }

    override suspend fun writeTo(t: AuthTokens, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = AuthTokens.serializer(),
                    value = t,
                ).encodeToByteArray(),
            )
        }
    }
}
