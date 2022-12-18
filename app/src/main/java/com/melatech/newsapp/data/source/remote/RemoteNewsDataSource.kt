package com.melatech.newsapp.data.source.remote

import com.melatech.newsapp.data.source.INewsDataSource
import com.melatech.newsapp.data.source.remote.api.NewsApi
import com.melatech.newsapp.data.source.remote.model.Article
import com.melatech.newsapp.data.source.remote.util.RetryPolicy
import com.melatech.newsapp.data.source.remote.util.retryWithPolicy
import com.melatech.newsapp.di.ApplicationScope
import com.melatech.newsapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RemoteNewsDataSource @Inject constructor(
    private val newsApi: NewsApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    @ApplicationScope private val externalScope: CoroutineScope,
    retryPolicy: RetryPolicy,
) : INewsDataSource {

    override val latestNewsApiResponseFlow: Flow<ServerResponse> =
        flow {
            val latestNewsApiResponse = newsApi.getTopHeadlines(COUNTRY_NAME, PAGE)
            val response = latestNewsApiResponse.body()?.articles?.let {
                if (it.isEmpty()) ServerResponse.NoContent
                else ServerResponse.Success(it)
            } ?: ServerResponse.NoContent
            emit(response)
        }
            .flowOn(ioDispatcher)
            .retryWithPolicy(retryPolicy)
            .catch { emit(ServerResponse.Failure(it.message)) }
            .shareIn(
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
    data class Success(val articles: List<Article>) : ServerResponse()
    data class Failure(val error: String?) : ServerResponse()
    object NoContent : ServerResponse()
}