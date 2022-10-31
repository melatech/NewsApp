package com.melatech.newsapp.di

import com.melatech.newsapp.data.source.INewsDataSource
import com.melatech.newsapp.data.source.INewsRepository
import com.melatech.newsapp.data.source.NewsRepository
import com.melatech.newsapp.data.source.remote.RemoteNewsDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    fun bindsNewsDataSource(
        newsDataSource: RemoteNewsDataSource
    ): INewsDataSource

    @Binds
    fun bindsNewsRepository(
        newsRepository: NewsRepository
    ): INewsRepository
}