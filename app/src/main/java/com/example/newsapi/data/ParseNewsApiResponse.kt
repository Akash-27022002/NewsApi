package com.example.newsapi.data

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * [parseNewsResponse] will help to parse the Stringify Json Object to the Kotlin classes
 * this function will convert the response string to the [NewsApiResponse]
 * */

fun parseNewsResponse(response: String): NewsApiResponse? {
    val jsonResponse = JSONObject(response)
    val status = jsonResponse.getString("status")
    val articlesArray = jsonResponse.getJSONArray("articles")

    if (status == "ok") {
        val articlesList = mutableListOf<Article>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        for (i in 0 until articlesArray.length()) {
            val articleObject = articlesArray.getJSONObject(i)
            val sourceObject = articleObject.getJSONObject("source")
            val source = Source(
                sourceObject.optString("id", null.toString()),
                sourceObject.getString("name")
            )
            val author = articleObject.optString("author", null.toString())
            val title = articleObject.getString("title")
            val description = articleObject.optString("description", null.toString())
            val url = articleObject.getString("url")
            val urlToImage = articleObject.optString("urlToImage", null.toString())
            val publishedAt = dateFormat.parse(articleObject.getString("publishedAt"))
            val content = articleObject.optString("content", null.toString())

            val article = Article(source, author, title, description, url, urlToImage, publishedAt, content)
            articlesList.add(article)
        }

        return NewsApiResponse(status,articlesList)
    } else {
        return null
    }
}
