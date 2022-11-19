package com.melatech.newsapp.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.melatech.newsapp.R

class NewsDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contentUrl = arguments?.getString(CONTENT_URL_KEY)
        val news_details_webview = view.findViewById<WebView>(R.id.news_details_webview)
        val news_details_progressBar = view.findViewById<ProgressBar>(R.id.news_details_progressBar)
        news_details_webview.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String) {
                    super.onPageFinished(view, url)
                    news_details_progressBar.visibility = View.GONE
                }
            }
            contentUrl?.run { loadUrl(this) }
        }
    }
}