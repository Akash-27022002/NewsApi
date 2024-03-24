package com.example.newsapi.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapi.R
import com.example.newsapi.data.Article
import com.example.newsapi.databinding.LayoutArticleBinding

class ArticlesAdapter (private val listener:(Article) -> Unit):ListAdapter<Article, ArticlesAdapter.ArticleViewHolder>(
    DiffCallback()
){
    inner class ArticleViewHolder(private val binding: LayoutArticleBinding):ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.invoke(getItem(adapterPosition))
            }
        }
        fun bind(article:Article){
            binding.apply {
                title.text = article.title
                pusblishedAt.text = root.context.getString(R.string.published_at,article.publishedAt.toString())
                if (article.description != "null"){
                    txtDescription.text = article.description
                }else{
                    txtDescription.text = article.content
                }
                source.text = root.context.getString(R.string.source_s,article.source.name)
                Glide.with(root)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.error_image)
                    .error(R.drawable.error_image)
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutArticleBinding.inflate(LayoutInflater.from(parent.context))
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class DiffCallback:DiffUtil.ItemCallback<Article>(){
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}