package com.melatech.newsapp.data.source

import com.melatech.newsapp.data.source.remote.model.ApiResponse
import retrofit2.Response

interface INewsDataSource {
    suspend fun getNewsHeadlines(country: String, page: Int): Response<ApiResponse>
}