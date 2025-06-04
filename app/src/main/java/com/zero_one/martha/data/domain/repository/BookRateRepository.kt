package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.domain.model.BookRate

interface BookRateRepository {
    suspend fun createBookRate(bookRate: BookRate): BookRate
    suspend fun updateBookRate(bookRate: BookRate): BookRate
    suspend fun deleteBookRate(bookRate: BookRate): Boolean
}
