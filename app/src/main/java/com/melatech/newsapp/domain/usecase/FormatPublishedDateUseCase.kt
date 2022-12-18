package com.melatech.newsapp.domain.usecase

import com.melatech.newsapp.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FormatPublishedDateUseCase @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter,
    private val zoneId: ZoneId,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    private suspend fun format(publishedDate: String): String {
        return withContext(defaultDispatcher) {
            val timestampInstant = Instant.parse(publishedDate)
            val articlePublishedZonedTime = ZonedDateTime.ofInstant(timestampInstant, zoneId)
            articlePublishedZonedTime.format(dateTimeFormatter)
        }
    }

    suspend operator fun invoke(publishedDate: String): String {
        return format(publishedDate)
    }
}