package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.model.Chapter
import com.zero_one.martha.data.domain.repository.BookRepository
import com.zero_one.martha.data.source.network.api.NetworkAPI
import com.zero_one.martha.data.source.network.models.Author
import com.zero_one.martha.data.source.network.models.Comment
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

    override suspend fun getBooksByQuery(query: String): List<Book> {
        try {
            val booksResult = api.getBooksByQuery(query)

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

    private fun networkCommentToComment(networkComment: Comment): com.zero_one.martha.data.domain.model.Comment {
        return com.zero_one.martha.data.domain.model.Comment(
            id = networkComment.id,
            text = networkComment.text,
            userId = networkComment.userId,
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
}
