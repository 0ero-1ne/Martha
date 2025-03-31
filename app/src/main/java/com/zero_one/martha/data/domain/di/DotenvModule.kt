package com.zero_one.martha.data.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.cdimascio.dotenv.Dotenv
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DotenvModule {

    @Provides
    @Singleton
    internal fun provideDotenv(): Dotenv {
        return Dotenv
            .configure()
            .directory("/assets")
            .filename("env")
            .load()
    }
}
