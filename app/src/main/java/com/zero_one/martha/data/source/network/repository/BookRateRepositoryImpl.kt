package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.model.BookRate
import com.zero_one.martha.data.domain.repository.BookRateRepository
import com.zero_one.martha.data.source.network.api.NetworkAPI
import javax.inject.Inject

class BookRateRepositoryImpl @Inject constructor(
    private val api: NetworkAPI
): BookRateRepository {
    override suspend fun createBookRate(bookRate: BookRate): BookRate {
        try {
            val createdBookRate = api.createBookRate(bookRateToNetwork(bookRate))

            if (createdBookRate.isSuccessful && createdBookRate.body() != null) {
                return networkToBookRate(createdBookRate.body()!!)
            }

            return BookRate()
        } catch (e: Exception) {
            Log.e("BookRateRepositoryImpl", "createBookRate", e)
            return BookRate()
        }
    }

    override suspend fun updateBookRate(bookRate: BookRate): BookRate {
        try {
            val updatedBookRate = api.updateBookRate(bookRateToNetwork(bookRate))

            if (updatedBookRate.isSuccessful && updatedBookRate.body() != null) {
                return networkToBookRate(updatedBookRate.body()!!)
            }

            return BookRate()
        } catch (e: Exception) {
            Log.e("BookRateRepositoryImpl", "updateBookRate", e)
            return BookRate()
        }
    }

    override suspend fun deleteBookRate(bookRate: BookRate): Boolean {
        try {
            val deletedBookRate = api.deleteBookRate(
                bookId = bookRate.bookId,
                userId = bookRate.userId,
            )

            return deletedBookRate.isSuccessful
        } catch (e: Exception) {
            Log.e("BookRateRepositoryImpl", "deleteBookRate", e)
            return false
        }
    }

    private fun bookRateToNetwork(bookRate: BookRate): com.zero_one.martha.data.source.network.models.BookRate {
        return com.zero_one.martha.data.source.network.models.BookRate(
            userId = bookRate.userId,
            bookId = bookRate.bookId,
            rating = bookRate.rating,
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
