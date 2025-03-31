package com.zero_one.martha.data.source.network.di

import com.zero_one.martha.data.source.datastore.user.tokens.TokensManager
import com.zero_one.martha.data.source.network.api.NetworkAPI
import com.zero_one.martha.data.source.network.api.authenticator.TokensAuthenticator
import com.zero_one.martha.data.source.network.api.authenticator.TokensInterceptor
import com.zero_one.martha.data.source.network.api.retrofit.RetrofitAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.cdimascio.dotenv.Dotenv
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTokensAuthenticator(
        tokensManager: TokensManager,
        dotenv: Dotenv
    ): Authenticator {
        return TokensAuthenticator(tokensManager, dotenv)
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        tokensAuthenticator: TokensAuthenticator,
        tokensManager: TokensManager
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            )
            .addInterceptor(TokensInterceptor(tokensManager))
            .authenticator(tokensAuthenticator)
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkAPI(
        httpClient: OkHttpClient,
        dotenv: Dotenv
    ): NetworkAPI {
        return Retrofit
            .Builder()
            .baseUrl(dotenv.get("api_host", "https://www.temp.api.com"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(RetrofitAPI::class.java)
    }
}
