package com.melatech.newsapp.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melatech.newsapp.data.source.INewsRepository
import com.melatech.newsapp.data.source.remote.model.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: INewsRepository
) : ViewModel() {

    private val _newsUiState: MutableStateFlow<List<ApiResponse>> = MutableStateFlow(emptyList())
    val newsUiState: StateFlow<List<ApiResponse>> = _newsUiState

    init {
        viewModelScope.launch {
            val response = repository.getNewsHeadlines("us", 1)
            val newsHeadlines = response.body()
            newsHeadlines?.let { _newsUiState.value = listOf(it) }
        }
    }
}