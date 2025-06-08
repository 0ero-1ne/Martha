package com.zero_one.martha.data.domain.di

import com.zero_one.martha.data.domain.repository.AuthRepository
import com.zero_one.martha.data.domain.repository.BookRateRepository
import com.zero_one.martha.data.domain.repository.BookRepository
import com.zero_one.martha.data.domain.repository.ChapterRepository
import com.zero_one.martha.data.domain.repository.CommentRateRepository
import com.zero_one.martha.data.domain.repository.CommentRepository
import com.zero_one.martha.data.domain.repository.StorageRepository
import com.zero_one.martha.data.domain.repository.TagRepository
import com.zero_one.martha.data.domain.repository.UserRepository
import com.zero_one.martha.data.source.network.repository.AuthRepositoryImpl
import com.zero_one.martha.data.source.network.repository.BookRateRepositoryImpl
import com.zero_one.martha.data.source.network.repository.BookRepositoryImpl
import com.zero_one.martha.data.source.network.repository.ChapterRepositoryImpl
import com.zero_one.martha.data.source.network.repository.CommentRateRepositoryImpl
import com.zero_one.martha.data.source.network.repository.CommentRepositoryImpl
import com.zero_one.martha.data.source.network.repository.StorageRepositoryImpl
import com.zero_one.martha.data.source.network.repository.TagRepositoryImpl
import com.zero_one.martha.data.source.network.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindBookRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ): BookRepository

    @Binds
    abstract fun bindChapterRepository(
        chapterRepositoryImpl: ChapterRepositoryImpl
    ): ChapterRepository

    @Binds
    abstract fun bindCommentRepository(
        commentRepositoryImpl: CommentRepositoryImpl
    ): CommentRepository

    @Binds
    abstract fun bindTagRepository(
        tagRepositoryImpl: TagRepositoryImpl
    ): TagRepository

    @Binds
    abstract fun bindCommentRateRepository(
        commentRateRepository: CommentRateRepositoryImpl
    ): CommentRateRepository

    @Binds
    abstract fun bindBookRateRepository(
        bookRateRepository: BookRateRepositoryImpl
    ): BookRateRepository

    @Binds
    abstract fun bindStorageRepository(
        storageRepository: StorageRepositoryImpl
    ): StorageRepository

}
