package com.zero_one.martha.data.domain.model

data class Filters(
    val tags: List<String>,
    val statuses: List<String>,
    val startYear: Int,
    val endYear: Int
)
