package com.melatech.newsapp.data.source.remote

import com.melatech.newsapp.data.source.INewsDataSource
import com.melatech.newsapp.data.source.remote.api.NewsApi
import com.melatech.newsapp.data.source.remote.model.Article
import com.melatech.newsapp.di.ApplicationScope
import com.melatech.newsapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteNewsDataSource @Inject constructor(
    private val newsApi: NewsApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    @ApplicationScope private val externalScope: CoroutineScope,
) : INewsDataSource {

    override val latestNewsApiResponseFlow: Flow<ServerResponse> =
        flow {
            try {
                val latestNewsApiResponse = withContext(ioDispatcher) {
                    newsApi.getTopHeadlines(COUNTRY_NAME, PAGE)
                }
                emit(ServerResponse.Success(latestNewsApiResponse.body()?.articles ?: emptyList()))
            } catch (e: Exception) {
                emit(ServerResponse.Failure(e.toString()))
            }
        }.shareIn(
            externalScope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )

    companion object {
        private const val COUNTRY_NAME = "us"
        private const val PAGE = 1
    }
}

sealed class ServerResponse {
    data class Success(val articles: List<Article> = emptyList()) : ServerResponse()
    data class Failure(val error: String) : ServerResponse()
}
