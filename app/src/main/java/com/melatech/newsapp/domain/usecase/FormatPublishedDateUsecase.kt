package com.melatech.newsapp.domain.usecase

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FormatPublishedDateUsecase @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter,
    private val zoneId: ZoneId
) {
    fun format(publishedDate: String): String {
        val timestampInstant = Instant.parse(publishedDate)
        val articlePublishedZonedTime =
            ZonedDateTime.ofInstant(timestampInstant, zoneId)
        return articlePublishedZonedTime.format(dateTimeFormatter)
    }
}