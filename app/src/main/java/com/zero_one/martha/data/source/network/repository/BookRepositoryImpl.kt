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
