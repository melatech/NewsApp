package com.melatech.newsapp.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.melatech.newsapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val CONTENT_URL_KEY: String = "CONTENT_URL_KEY"

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private val newsViewModel by viewModels<NewsViewModel>()

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapter = NewsAdapter { url: String -> navigateToDetailsScreen(view, url) }
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = newsAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getNewsHeadlines()
    }

    private fun navigateToDetailsScreen(view: View, url: String) {
        val bundle = bundleOf(CONTENT_URL_KEY to url)
        Navigation.findNavController(view).navigate(R.id.navigateToNewsDetailsFragment, bundle)
    }

    private fun getNewsHeadlines() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsViewModel.newsUiState.collect { uiState ->
                    if (uiState.isNotEmpty()) {
                        newsAdapter.submitList(uiState)
                    }
                }
            }
        }
    }
}