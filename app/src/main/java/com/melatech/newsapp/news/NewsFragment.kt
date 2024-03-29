package com.melatech.newsapp.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.melatech.newsapp.R
import com.melatech.newsapp.news.ErrorType.*
import com.melatech.newsapp.news.model.ContentUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val CONTENT_URL_KEY: String = "CONTENT_URL_KEY"

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private val newsViewModel by viewModels<NewsViewModel>()

    private lateinit var newsAdapter: NewsAdapter

    private lateinit var noConnectionView: LinearLayout
    private lateinit var newsListView: RecyclerView
    private lateinit var errorMessage: TextView
    private lateinit var errorIcon: ImageView
    private lateinit var newsListProgressBar: ProgressBar
    private lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapter = NewsAdapter { navigateToContent(view) }
        newsListView = view.findViewById(R.id.news_recycler_view)
        noConnectionView = view.findViewById(R.id.no_connection_error_view)
        errorMessage = view.findViewById(R.id.error_title)
        errorIcon = view.findViewById(R.id.error_icon)
        newsListProgressBar = view.findViewById(R.id.news_list_progressBar)
        swipeContainer = view.findViewById(R.id.swipe_container)
        newsListView.adapter = newsAdapter

        swipeContainer.setOnRefreshListener {
            newsViewModel.refreshNews()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    newsViewModel.newsUiStateFlow.collect { newsUiState ->
                        swipeContainer.isRefreshing = false
                        when (newsUiState) {
                            is NewsUiState.Data -> {
                                newsListProgressBar.visibility = View.GONE
                                noConnectionView.visibility = View.GONE
                                newsListView.visibility = View.VISIBLE
                                swipeContainer.visibility = View.VISIBLE
                                newsAdapter.submitList(newsUiState.newsList)
                            }
                            is NewsUiState.Error -> {
                                when (newsUiState.errorType) {
                                    NO_CONTENT -> {
                                        newsListProgressBar.visibility = View.GONE
                                        noConnectionView.visibility = View.VISIBLE
                                        errorIcon.visibility = View.VISIBLE
                                        errorIcon.setImageResource(R.drawable.no_news)
                                        newsListView.visibility = View.GONE
                                        swipeContainer.visibility = View.GONE
                                        errorMessage.text = "No News"
                                    }
                                    NO_NETWORK_ERROR -> {
                                        newsListProgressBar.visibility = View.GONE
                                        noConnectionView.visibility = View.VISIBLE
                                        errorIcon.visibility = View.VISIBLE
                                        errorIcon.setImageResource(R.drawable.no_internet)
                                        newsListView.visibility = View.GONE
                                        swipeContainer.visibility = View.GONE
                                        errorMessage.text = "No Internet Connection"
                                    }
                                    RETRY_GENERIC_ERROR -> {
                                        newsListProgressBar.visibility = View.GONE
                                        noConnectionView.visibility = View.GONE
                                        newsListView.visibility = View.GONE
                                        swipeContainer.visibility = View.GONE

                                        val builder = AlertDialog.Builder(requireContext())

                                        with(builder)
                                        {
                                            setTitle("Error")
                                            setMessage("Cannot connect to server")
                                            setPositiveButton("OK") { _, _ -> requireActivity().finish() }
                                            show()
                                        }
                                    }
                                }
                            }
                            NewsUiState.Loading -> {
                                newsListProgressBar.visibility = View.VISIBLE
                                noConnectionView.visibility = View.GONE
                                newsListView.visibility = View.GONE
                                swipeContainer.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ContentUrl.navigateToContent(view: View) {
        val bundle = bundleOf(CONTENT_URL_KEY to this)
        Navigation.findNavController(view).navigate(R.id.navigateToNewsDetailsFragment, bundle)
    }
}