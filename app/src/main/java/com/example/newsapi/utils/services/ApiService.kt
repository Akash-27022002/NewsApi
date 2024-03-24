package com.example.newsapi.utils.services

import android.util.Log
import com.example.newsapi.data.NewsApiResponse
import com.example.newsapi.data.parseNewsResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface ApiService {
    companion object {
        private const val API_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
        private const val TAG = "APIResponse"
        fun getInstance(): ApiService {
            return object : ApiService {}
        }
    }


    /**
     *  [getNewsArticles] is the that fetch articles from [API_URL]
     *  this function does not use any third party thing for api calling using all the default thing that
     *  would be provide by the android itself as as [HttpURLConnection]
     **/

    suspend fun getNewsArticles(): NewsApiResponse? {
        try {
            val url = URL(API_URL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            Log.i(TAG, "Response Code $responseCode")
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                /**
                 * [parseNewsResponse] is helps to convert the string [response] to [NewsApiResponse]
                 * that will directly parse to the Data class the would be easy to use in Kotlin
                 * */

                return parseNewsResponse(response.toString())
            } else {
                throw Exception("Failed to fetch news articles. Response code: $responseCode")
            }
        }catch (e:Exception){
            return  null
        }
    }

}
