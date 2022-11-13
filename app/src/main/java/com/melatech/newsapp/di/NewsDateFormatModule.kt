package com.melatech.newsapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsDateFormatModule {

    @Provides
    @Singleton
    fun provideDateTimeFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault())
    }

    @Provides
    @Singleton
    fun provideZoneId(): ZoneId {
        return ZoneId.systemDefault()
    }
}