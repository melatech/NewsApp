package com.melatech.newsapp.news.model

data class ArticleUIModel(
    val id: Int,
    val title: String,
    val description: String,
    val formattedPublishedDate: String,
    val authorName: String
)