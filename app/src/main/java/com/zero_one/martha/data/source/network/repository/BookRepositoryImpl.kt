package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.model.BookRate
import com.zero_one.martha.data.domain.model.Chapter
import com.zero_one.martha.data.domain.model.CommentRate
import com.zero_one.martha.data.domain.model.Filters
import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.data.domain.repository.BookRepository
import com.zero_one.martha.data.source.network.api.NetworkAPI
import com.zero_one.martha.data.source.network.models.Author
import com.zero_one.martha.data.source.network.models.Tag
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: NetworkAPI
): BookRepository {
    override suspend fun getBooks(): List<Book> {
        try {
            val booksResult = api.getBooks()

            if (booksResult.isSuccessful && booksResult.body() != null) {
                return booksResult.body()!!.map {book ->
                    networkBookToBook(book)
                }
            }

            return emptyList()
        } catch (e: Exception) {
            Log.e("BookRepositoryImpl", "getBooks()", e)
            return emptyList()
        }
    }

    override suspend fun getBookById(id: UInt): Book {
        try {
            val bookResult = api.getBookById(id)

            if (bookResult.isSuccessful && bookResult.body() != null) {
                return networkBookToBook(bookResult.body()!!)
            }

            return Book()
        } catch (e: Exception) {
            Log.e("BookRepositoryImpl", "getBookById()", e)
            return Book()
        }
    }

    override suspend fun getBookForReader(id: UInt): Book {
        try {
            val bookResult = api.getBookForReader(id)

            if (bookResult.isSuccessful && bookResult.body() != null) {
                return networkBookToBook(bookResult.body()!!)
            }

            return Book()
        } catch (e: Exception) {
            Log.e("BookRepositoryImpl", "getBookForReader()", e)
            return Book()
        }
    }

    override suspend fun getBooksByQuery(query: String, filters: Filters): List<Book> {
        try {
            val booksResult = api.getBooksByQuery(
                query = query,
                tags = filters.tags.joinToString(","),
            )

            if (booksResult.isSuccessful && booksResult.body() != null) {
                return booksResult.body()!!.map {
                    networkBookToBook(it)
                }
            }

            return emptyList()
        } catch (e: Exception) {
            Log.e("BookRepositoryImpl", "getBooksByQuery", e)
            return emptyList()
        }
    }

    private fun networkBookToBook(networkBook: com.zero_one.martha.data.source.network.models.Book): Book {
        return Book(
            id = networkBook.id,
            title = networkBook.title,
            description = networkBook.description,
            status = networkBook.description,
            year = networkBook.year,
            views = networkBook.views,
            cover = networkBook.cover,
            tags = networkBook.tags?.map {networkTagToTag(it)} ?: emptyList(),
            authors = networkBook.authors?.map {networkAuthorToAuthor(it)} ?: emptyList(),
            comments = networkBook.comments?.map {networkCommentToComment(it)} ?: emptyList(),
            chapters = networkBook.chapters?.map {networkChapterToChapter(it)} ?: emptyList(),
            rates = networkBook.rates?.map {networkToBookRate(it)} ?: emptyList(),
        )
    }

    private fun networkTagToTag(networkTag: Tag): com.zero_one.martha.data.domain.model.Tag {
        return com.zero_one.martha.data.domain.model.Tag(
            title = networkTag.title,
            id = networkTag.id,
        )
    }

    private fun networkAuthorToAuthor(networkAuthor: Author): com.zero_one.martha.data.domain.model.Author {
        return com.zero_one.martha.data.domain.model.Author(
            id = networkAuthor.id,
            fullname = networkAuthor.fullname,
            biography = networkAuthor.biography,
            image = networkAuthor.image,
        )
    }

    private fun networkCommentToComment(comment: com.zero_one.martha.data.source.network.models.Comment): com.zero_one.martha.data.domain.model.Comment {
        return com.zero_one.martha.data.domain.model.Comment(
            id = comment.id,
            parentId = comment.parentId,
            bookId = comment.bookId,
            userId = comment.userId,
            text = comment.text,
            rates = comment.rates?.map {networkCommentRateToCommentRate(it)} ?: listOf(),
            replies = comment.replies?.map {
                networkCommentToComment(it)
            } ?: listOf(),
            user = User(
                id = comment.user.id,
                username = comment.user.username,
                email = comment.user.email,
                image = comment.user.image,
            ),
        )
    }

    private fun networkCommentRateToCommentRate(commentRate: com.zero_one.martha.data.source.network.models.CommentRate): CommentRate {
        return CommentRate(
            commentId = commentRate.commentId,
            userId = commentRate.userId,
            rating = commentRate.rating,
            user = User(
                id = commentRate.user.id,
                username = commentRate.user.username,
                email = commentRate.user.email,
                image = commentRate.user.image,
            ),
        )
    }

    private fun networkChapterToChapter(chapter: com.zero_one.martha.data.source.network.models.Chapter): Chapter {
        return Chapter(
            id = chapter.id,
            title = chapter.title,
            serial = chapter.serial,
            text = chapter.text,
            audio = chapter.audio,
            bookId = chapter.bookId,
        )
    }

    private fun networkToBookRate(bookRate: com.zero_one.martha.data.source.network.models.BookRate): BookRate {
        return BookRate(
            userId = bookRate.userId,
            bookId = bookRate.bookId,
            rating = bookRate.rating,
        )
    }
}
