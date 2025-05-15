package com.zero_one.martha.data.source.network.api.authenticator

import com.zero_one.martha.BuildConfig
import com.zero_one.martha.data.source.datastore.user.tokens.TokensManager
import com.zero_one.martha.data.source.network.api.retrofit.RetrofitAPI
import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import com.zero_one.martha.data.source.network.models.auth.UpdateToken
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class TokensAuthenticator @Inject constructor(
    private val tokensManager: TokensManager
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            if (response.code == 401) {
                val refreshToken = tokensManager.getRefreshToken()
                val newAuthTokens = getUpdatedToken(refreshToken)

                if (newAuthTokens == null) {
                    tokensManager.removeTokens()
                    return@runBlocking null
                } else {
                    tokensManager.setTokens(newAuthTokens)
                    val newRequest = buildRequest(
                        requestBuilder = response.request.newBuilder(),
                        accessToken = tokensManager.getAccessToken(),
                    )

                    return@runBlocking newRequest
                }
            }

            return@runBlocking response.request
        }
    }

    private fun buildRequest(requestBuilder: Request.Builder, accessToken: String): Request {
        return requestBuilder
            .header("Content-type", "application/json")
            .header("Authorization", "Bearer $accessToken")
            .build()
    }

    private suspend fun getUpdatedToken(refreshToken: String): AuthTokens? {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient()
                    .newBuilder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        },
                    )
                    .build(),
            )
            .build()

        val repo = retrofit.create(RetrofitAPI::class.java)
        val newTokens = repo.refresh(
            UpdateToken(
                refreshToken = refreshToken,
            ),
        )

        if (!newTokens.isSuccessful) {
            return null
        }

        return newTokens.body()!!
    }
}
