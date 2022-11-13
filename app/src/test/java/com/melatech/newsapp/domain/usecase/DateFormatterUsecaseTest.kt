package com.melatech.newsapp.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class DateFormatterUsecaseTest {

    private val dateFormatterUsecase = FormatPublishedDateUsecase(
        dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.ENGLISH),
        zoneId = ZoneId.of("Europe/London")
    )

    @Test
    fun testFormatPublishedDate() {
        val formattedDate = dateFormatterUsecase.format("2022-11-13T12:00:42Z")
        assertEquals("Nov 13, 2022, 12:00:42 PM", formattedDate)
    }
}