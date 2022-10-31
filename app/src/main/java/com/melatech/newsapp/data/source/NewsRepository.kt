package com.melatech.newsapp.data.source

import com.melatech.newsapp.data.source.remote.model.ApiResponse
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val dataSource: INewsDataSource
) : INewsRepository {

    override suspend fun getNewsHeadlines(country: String, page: Int): Response<ApiResponse> {
        return dataSource.getNewsHeadlines(country, page)
    }
}