package com.zero_one.martha.data.source.datastore.user

import androidx.datastore.core.Serializer
import com.zero_one.martha.data.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserSerializer: Serializer<User> {
    override val defaultValue: User
        get() = User()

    override suspend fun readFrom(input: InputStream): User {
        return try {
            Json.decodeFromString(
                deserializer = User.serializer(),
                string = input.readBytes().decodeToString(),
            )
        } catch (error: SerializationException) {
            error.printStackTrace()
            User()
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = User.serializer(),
                    value = t,
                ).encodeToByteArray(),
            )
        }
    }
}
