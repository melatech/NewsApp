package com.melatech.newsapp.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class FormatPublishedDateUseCaseTest {

    private val formatPublishedDateUsecase = FormatPublishedDateUseCase(
        dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.ENGLISH),
        zoneId = ZoneId.of("Europe/London")
    )

    @Test
    fun testFormatPublishedDate() {
        runBlocking {
            val formattedDate = formatPublishedDateUsecase("2022-11-13T12:00:42Z")
            assertEquals("Nov 13, 2022, 12:00:42 PM", formattedDate)
        }
    }
}