package com.melatech.newsapp.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melatech.newsapp.data.source.INewsRepository
import com.melatech.newsapp.data.source.remote.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: INewsRepository
) : ViewModel() {

    private val _newsUiState: MutableStateFlow<List<Article>> = MutableStateFlow(emptyList())
    val newsUiState: StateFlow<List<Article>> = _newsUiState

    init {
        viewModelScope.launch {
            val response = repository.getNewsHeadlines(COUNTRY_NAME, PAGE)
            val newsHeadlines = response.body()
            newsHeadlines?.let { _newsUiState.value = it.articles }
        }
    }

    companion object {
        private val COUNTRY_NAME = "us"
        private val PAGE = 1
    }
}