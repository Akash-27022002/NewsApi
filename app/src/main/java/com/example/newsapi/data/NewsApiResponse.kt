package com.example.newsapi.data

data class NewsApiResponse(
    val status: String,
    val articles: List<Article>
)
