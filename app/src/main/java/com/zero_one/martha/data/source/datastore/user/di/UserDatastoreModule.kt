package com.zero_one.martha.data.source.datastore.user.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.data.source.datastore.user.UserManager
import com.zero_one.martha.data.source.datastore.user.UserSerializer
import com.zero_one.martha.data.source.datastore.user.tokens.TokensManager
import com.zero_one.martha.data.source.datastore.user.tokens.TokensSerializer
import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDatastoreModule {

    @Provides
    @Singleton
    internal fun provideTokensSerializer(): TokensSerializer = TokensSerializer

    @Provides
    @Singleton
    internal fun provideTokensDataStore(
        @ApplicationContext context: Context,
        tokensSerializer: TokensSerializer
    ): DataStore<AuthTokens> = DataStoreFactory.create(
        serializer = tokensSerializer,
    ) {
        context.dataStoreFile("tokens_store.pb")
    }

    @Provides
    @Singleton
    internal fun provideTokensManager(
        tokensDataStore: DataStore<AuthTokens>
    ): TokensManager {
        return TokensManager(tokensDataStore)
    }

    @Provides
    @Singleton
    internal fun provideUserSerializer(): UserSerializer = UserSerializer

    @Provides
    @Singleton
    internal fun provideUserDataStore(
        @ApplicationContext context: Context,
        userSerializer: UserSerializer
    ): DataStore<User> = DataStoreFactory.create(
        serializer = userSerializer,
    ) {
        context.dataStoreFile("user_store.pb")
    }

    @Provides
    @Singleton
    internal fun provideUserManager(
        userDataStore: DataStore<User>
    ): UserManager {
        return UserManager(userDataStore = userDataStore)
    }
}
