package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.model.Filters

interface BookRepository {
    suspend fun getBooks(): List<Book>
    suspend fun getBookById(id: UInt): Book
    suspend fun getBookForReader(id: UInt): Book
    suspend fun getBooksByQuery(query: String, filters: Filters): List<Book>
}
