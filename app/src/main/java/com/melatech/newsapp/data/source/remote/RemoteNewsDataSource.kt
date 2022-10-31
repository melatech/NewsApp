package com.melatech.newsapp.data.source.remote

import com.melatech.newsapp.data.source.INewsDataSource
import com.melatech.newsapp.data.source.remote.api.NewsApi
import com.melatech.newsapp.data.source.remote.model.ApiResponse
import com.melatech.newsapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RemoteNewsDataSource @Inject constructor(
    private val newsApi: NewsApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : INewsDataSource {

    override suspend fun getNewsHeadlines(country: String, page: Int): Response<ApiResponse> {
        return withContext(ioDispatcher) {
            newsApi.getTopHeadlines(country, page)
        }
    }
}