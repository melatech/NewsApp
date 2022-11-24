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
    private val getConnectionUpdateStatusUseCase: GetConnectionUpdateStatusUseCase,
) : ViewModel() {

    private val _newsUiStateFlow: MutableStateFlow<NewsUiState> =
        MutableStateFlow(NewsUiState.Data(emptyList()))
    val newsUiStateFlow: StateFlow<NewsUiState> = _newsUiStateFlow

    init {
        viewModelScope.launch {
            getConnectionUpdateStatusUseCase.networkStatusFlow
                .distinctUntilChanged()
                .collectLatest { networkStatus ->
                    when (networkStatus) {
                        NetworkStatus.Available -> getNewsHeadlines()
                        NetworkStatus.Unavailable ->
                            _newsUiStateFlow.value = NewsUiState.Error(ErrorType.NETWORK_ERROR)
                    }
                }
        }
    }

    private suspend fun getNewsHeadlines() {
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
                _newsUiStateFlow.value = NewsUiState.Data(articleUIModelList)
            }
        } catch (e: Exception) {
            _newsUiStateFlow.value = NewsUiState.Error(ErrorType.GENERIC_ERROR)
        }
    }

    companion object {
        private const val COUNTRY_NAME = "us"
        private const val PAGE = 1
    }
}

sealed class NewsUiState {
    data class Data(val newsList: List<ArticleUIModel>) : NewsUiState()
    data class Error(val errorType: ErrorType) : NewsUiState()
}

enum class ErrorType {
    GENERIC_ERROR, NETWORK_ERROR
}
