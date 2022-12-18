package com.melatech.newsapp.data.source

import com.melatech.newsapp.data.source.remote.ServerResponse
import kotlinx.coroutines.flow.Flow

interface INewsRepository {
    val latestNewsApiResponseFlow: Flow<ServerResponse>
}