package com.zero_one.martha.data.source.network.api.authenticator

import com.zero_one.martha.data.source.datastore.user.tokens.TokensManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokensInterceptor @Inject constructor(
    private val tokensManager: TokensManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val token = runBlocking {
            return@runBlocking tokensManager.getAccessToken()
        }
        request.addHeader("Authorization", "Bearer $token")
        request.addHeader("Accept", "application/json")
        
        return chain.proceed(request.build())
    }
}
