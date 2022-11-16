package com.melatech.newsapp.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melatech.newsapp.data.source.INewsRepository
import com.melatech.newsapp.domain.usecase.FormatPublishedDateUsecase
import com.melatech.newsapp.news.model.ArticleUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: INewsRepository,
    private val formatPublishedDateUsecase: FormatPublishedDateUsecase
) : ViewModel() {

    private val _newsUiState: MutableStateFlow<List<ArticleUIModel>> = MutableStateFlow(emptyList())
    val newsUiState: StateFlow<List<ArticleUIModel>> = _newsUiState

    init {
        viewModelScope.launch {
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
                                formatPublishedDateUsecase(publishedDate)
                            } ?: "-",
                            authorName = article.author ?: "-"
                        )
                    }
                _newsUiState.value = articleUIModelList
            }
        }
    }

    companion object {
        private val COUNTRY_NAME = "us"
        private val PAGE = 1
    }
}