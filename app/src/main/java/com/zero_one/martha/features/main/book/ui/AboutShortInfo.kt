package com.zero_one.martha.features.main.book.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zero_one.martha.data.domain.model.Book
import java.text.DecimalFormat
import java.util.Locale
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

@Composable
fun AboutShortInfo(
    modifier: Modifier = Modifier,
    book: Book
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        ShortInfoItem(
            title = "Year",
            text = book.year.toString(),
        )
        ShortInfoItem(
            title = "Status",
            text = book.status,
        )
        ShortInfoItem(
            title = "Views",
            text = prettyCount(book.views),
        )

        var rating = book.rates.sumOf {it.rating}.toFloat() / book.rates.size.toFloat()
        if (rating.isNaN()) {
            rating = 0f
        }
        val ratingString = String.format(Locale.US, "%.2f", rating)
        ShortInfoItem(
            title = "Rating",
            text = ratingString.let {
                if (it.endsWith("00")) {
                    ratingString.substringBefore(".")
                } else if (it.endsWith("0")) {
                    ratingString.substringBefore("0")
                } else {
                    ratingString
                }
            },
        )
    }
}

@Composable
private fun ShortInfoItem(
    title: String,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 5.dp),
        )
        Text(
            text = text,
        )
    }
}

private fun prettyCount(number: Int): String {
    val suffix = arrayOf(' ', 'K', 'M', 'B', 'T', 'P', 'E')
    val numValue = number.toLong()
    val value = floor(log10(numValue.toDouble())).toInt()
    val base = value / 3
    return if (value >= 3 && base < suffix.size) {
        DecimalFormat("#0.0").format(numValue / 10.0.pow(base * 3)) + suffix[base]
    } else {
        DecimalFormat("#,##0").format(numValue)
    }
}
