package com.melatech.newsapp.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.melatech.newsapp.R
import com.melatech.newsapp.data.source.remote.model.Article
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter : ListAdapter<Article, NewsAdapter.NewsViewHolder>(NewsDiffCallback) {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val news_title: TextView = itemView.findViewById(R.id.news_title)
        private val news_description: TextView = itemView.findViewById(R.id.news_description)
        private val news_author: TextView = itemView.findViewById(R.id.news_author)
        private val news_date: TextView = itemView.findViewById(R.id.news_date)

        fun bind(article: Article) {
            news_title.text = article.title
            news_description.text = article.description
            news_author.text = article.author ?: "-"

            article.publishedAt?.apply {
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault())
                val d = formatter.parse(this)
                news_date.text = d?.let {
                    val f = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    f.format(d)
                } ?: "-"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item_layout, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }
}

object NewsDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }
}
