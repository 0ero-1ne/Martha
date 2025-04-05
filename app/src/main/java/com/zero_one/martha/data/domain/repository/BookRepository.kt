package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.domain.model.Book

interface BookRepository {
    suspend fun getBooks(): List<Book>
    suspend fun getBookById(id: UInt): Book
}
