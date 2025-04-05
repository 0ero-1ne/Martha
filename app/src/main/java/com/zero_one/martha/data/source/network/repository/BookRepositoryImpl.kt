package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.repository.BookRepository
import com.zero_one.martha.data.source.network.api.NetworkAPI
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: NetworkAPI
): BookRepository {
    override suspend fun getBooks(): List<Book> {
        try {
            val booksResult = api.getBooks()
            Log.d("BOOK_REPOSITORY before", "1")

            if (booksResult.isSuccessful && booksResult.body() != null) {
                val books = booksResult.body()!!.map {book ->
                    networkBookToBook(book)
                }

                Log.d("BOOK_REPOSITORY", books[0].title)
                Log.d("BOOK_REPOSITORY books length", books.size.toString())
                return books
            }

            Log.d("BOOK_REPOSITORY after", "2")
            return emptyList()
        } catch (e: Exception) {
            Log.e("BookRepositoryImpl", "getBooks()", e)
            return emptyList()
        }
    }

    override suspend fun getBookById(id: UInt): Book {
        TODO("Not yet implemented")
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
        )
    }
}
