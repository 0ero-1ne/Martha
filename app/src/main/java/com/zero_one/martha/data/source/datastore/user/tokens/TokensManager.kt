package com.zero_one.martha.data.source.datastore.user.tokens

import androidx.datastore.core.DataStore
import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TokensManager @Inject constructor(
    private val tokensDataStore: DataStore<AuthTokens>
) {
    suspend fun hasTokens(): Boolean {
        return tokensDataStore.data.first().accessToken != ""
            && tokensDataStore.data.first().refreshToken != ""
    }

    suspend fun getAccessToken(): String {
        return tokensDataStore.data.first().accessToken
    }

    suspend fun getRefreshToken(): String {
        return tokensDataStore.data.first().refreshToken
    }

    suspend fun setTokens(tokens: AuthTokens) {
        tokensDataStore.updateData {
            tokens
        }
    }

    suspend fun removeTokens() {
        tokensDataStore.updateData {
            AuthTokens("", "")
        }
    }
}
