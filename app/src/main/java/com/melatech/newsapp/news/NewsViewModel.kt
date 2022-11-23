package com.melatech.newsapp.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melatech.newsapp.data.source.INewsRepository
import com.melatech.newsapp.domain.usecase.FormatPublishedDateUseCase
import com.melatech.newsapp.domain.usecase.GetConnectionUpdateStatusUseCase
import com.melatech.newsapp.domain.usecase.NetworkStatus
import com.melatech.newsapp.news.model.ArticleUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: INewsRepository,
    private val formatPublishedDateUseCase: FormatPublishedDateUseCase,
    getConnectionUpdateStatusUseCase: GetConnectionUpdateStatusUseCase,
) : ViewModel() {

    private val _newsUiStateFlow: MutableStateFlow<List<ArticleUIModel>> =
        MutableStateFlow(emptyList())
    val newsUiStateFlow: StateFlow<List<ArticleUIModel>> = _newsUiStateFlow

    val networkStatusFlow: Flow<NetworkStatus> = getConnectionUpdateStatusUseCase.networkStatusFlow

    init {
        viewModelScope.launch {
            try {
                val response = repository.getNewsHeadlines(COUNTRY_NAME, PAGE)
                val newsHeadlines = response.body()
                newsHeadlines?.run {
                    val articleUIModelList = this.articles
                        .map { article ->
                            ArticleUIModel(
                                id = article.id ?: 0,
                                title = article.title ?: "-",
                                description = article.description ?: "-",
                                formattedPublishedDate = article.publishedAt?.let { publishedDate ->
                                    formatPublishedDateUseCase(publishedDate)
                                } ?: "-",
                                authorName = article.author ?: "-",
                                contentUrl = article.url
                            )
                        }
                    _newsUiStateFlow.value = articleUIModelList
                }
            } catch (e: Exception) {
                println("thiru error - $e")
            }

        }
    }

    companion object {
        private const val COUNTRY_NAME = "us"
        private const val PAGE = 1
    }
}