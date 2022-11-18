package com.melatech.newsapp.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
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
        news_details_webview.apply {
            webViewClient = WebViewClient()
            contentUrl?.run { loadUrl(this) }
        }
    }
}