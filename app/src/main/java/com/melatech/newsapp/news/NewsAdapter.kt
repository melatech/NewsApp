package com.melatech.newsapp.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.melatech.newsapp.R
import com.melatech.newsapp.news.model.ArticleUIModel

class NewsAdapter(val onItemClick: () -> Unit) :
    ListAdapter<ArticleUIModel, NewsAdapter.NewsViewHolder>(NewsDiffCallback) {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val news_title: TextView = itemView.findViewById(R.id.news_title)
        private val news_description: TextView = itemView.findViewById(R.id.news_description)
        private val news_author: TextView = itemView.findViewById(R.id.news_author)
        private val news_date: TextView = itemView.findViewById(R.id.news_date)

        fun bind(articleUIModel: ArticleUIModel) {
            news_title.text = articleUIModel.title
            news_description.text = articleUIModel.description
            news_author.text = articleUIModel.authorName
            news_date.text = articleUIModel.formattedPublishedDate
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
        holder.itemView.setOnClickListener { onItemClick() }
    }
}

object NewsDiffCallback : DiffUtil.ItemCallback<ArticleUIModel>() {
    override fun areItemsTheSame(oldItem: ArticleUIModel, newItem: ArticleUIModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ArticleUIModel, newItem: ArticleUIModel): Boolean {
        return oldItem.id == newItem.id
    }
}
