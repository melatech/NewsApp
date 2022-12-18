package com.melatech.newsapp.data.source

import com.melatech.newsapp.data.source.remote.ServerResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    dataSource: INewsDataSource,
) : INewsRepository {
    override val latestNewsApiResponseFlow: Flow<ServerResponse> =
        dataSource.latestNewsApiResponseFlow
}