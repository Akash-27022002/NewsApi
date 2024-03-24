package com.example.newsapi.utils.apiClient

import android.util.Log
import com.example.newsapi.data.Article
import com.example.newsapi.utils.services.ApiService

/**
 * [NewsApiClient] via this viewModel and ApiService will communicate to get the data
 * it will return the List of [Article] that would simplified to show data iin ui
 * */

class NewsApiClient(private val apiService: ApiService) {
    suspend fun getNews():List<Article>?
    {
        return try {
            apiService.getNewsArticles()!!.articles
        } catch (e: Exception) {
            null
        }
    }


}