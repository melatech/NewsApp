package com.melatech.newsapp.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melatech.newsapp.data.source.INewsRepository
import com.melatech.newsapp.data.source.remote.ServerResponse
import com.melatech.newsapp.domain.usecase.FormatPublishedDateUseCase
import com.melatech.newsapp.domain.usecase.GetConnectionUpdateStatusUseCase
import com.melatech.newsapp.domain.usecase.NetworkStatus
import com.melatech.newsapp.news.model.ArticleUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NewsViewModel @Inject constructor(
    repository: INewsRepository,
    private val formatPublishedDateUseCase: FormatPublishedDateUseCase,
    getConnectionUpdateStatusUseCase: GetConnectionUpdateStatusUseCase,
) : ViewModel() {

    private val _newsUiStateFlow: MutableStateFlow<NewsUiState> =
        MutableStateFlow(NewsUiState.Data(emptyList()))
    val newsUiStateFlow: StateFlow<NewsUiState> = _newsUiStateFlow

    private val latestNewsDataflow: Flow<NewsUiState> =
        repository.latestNewsApiResponseFlow
            .map { serverResponse ->
                when (serverResponse) {
                    is ServerResponse.NoContent -> NewsUiState.Error(ErrorType.NO_CONTENT)
                    is ServerResponse.Success -> {
                        val articleUIModelList = serverResponse.articles
                            .map { article ->
                                ArticleUIModel(id = article.id ?: 0,
                                    title = article.title ?: "-",
                                    description = article.description ?: "-",
                                    formattedPublishedDate = article.publishedAt?.let { publishedDate ->
                                        formatPublishedDateUseCase(publishedDate)
                                    } ?: "-",
                                    authorName = article.author ?: "-",
                                    contentUrl = article.url)
                            }
                        NewsUiState.Data(articleUIModelList)
                    }
                    is ServerResponse.Failure -> NewsUiState.Error(ErrorType.RETRY_GENERIC_ERROR)
                }
            }
            .shareIn(
                viewModelScope,
                replay = 1,
                started = SharingStarted.Lazily
            )

    init {
        _newsUiStateFlow.value = NewsUiState.Loading
        viewModelScope.launch {
            getConnectionUpdateStatusUseCase.networkStatusFlow
                .distinctUntilChanged()
                .flatMapLatest { networkStatus ->
                    when (networkStatus) {
                        NetworkStatus.Available -> latestNewsDataflow
                        NetworkStatus.Unavailable -> flow { emit(NewsUiState.Error(ErrorType.NO_NETWORK_ERROR)) }
                    }
                }
                .collect { _newsUiStateFlow.value = it }
        }
    }
}

sealed class NewsUiState {
    data class Data(val newsList: List<ArticleUIModel>) : NewsUiState()
    object Loading : NewsUiState()
    data class Error(val errorType: ErrorType) : NewsUiState()
}

enum class ErrorType {
    NO_CONTENT,
    NO_NETWORK_ERROR,
    RETRY_GENERIC_ERROR
}
