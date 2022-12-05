package com.melatech.newsapp.news.ui.model

data class ArticleUIModel(
    val id: Int,
    val title: String,
    val description: String,
    val formattedPublishedDate: String,
    val authorName: String,
    val contentUrl: ContentUrl?
)

typealias ContentUrl = String