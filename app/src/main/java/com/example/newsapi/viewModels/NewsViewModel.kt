package com.example.newsapi.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapi.data.Article
import com.example.newsapi.data.Sorting
import com.example.newsapi.utils.apiClient.NewsApiClient
import com.example.newsapi.utils.services.ApiService

/**
 *  This [NewsViewModel] will managing the articles and there sorting
 *  the loading and errors in the api's across all over the application
 *
 *  [_articles] contains all of the new article get by the API [NewsApiClient]
 *      and this the [MutableLiveData]
 *
 *  [_error] will mange the errors in the application via [NewsApiClient] API so it will
 *  display some error dialog
 *
 *  [_loading] this will manage the loading screen till api will fail or success
 *
 *  [_sorting] this helps us to sort the article according to date from new to old and old to new.
 *
 * */

class NewsViewModel:ViewModel() {
    private val _articles = MutableLiveData<List<Article>>()
    val articles : LiveData<List<Article>>
        get() = _articles

    private val _error = MutableLiveData<String>()
    val error : LiveData<String>
        get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>
        get() = _loading

    private val _sorting = MutableLiveData<Sorting>()
    val sorting :LiveData<Sorting>
        get() = _sorting

    suspend fun getNewsList(){
        try {
            _loading.postValue(true)
            val response = apiClient.getNews()
            response?.let {
                _articles.postValue(it)
            }?:run {
                _error.postValue("Failed to Load")
            }
        }catch (e : Exception){
            _error.postValue(e.message)
        }finally {
            _loading.postValue(false)
        }
    }

    fun shortArticle(sort: Sorting){
        _sorting.postValue(sort)
    }

    companion object{
        private val apiService = ApiService.getInstance()
        private  val apiClient = NewsApiClient(apiService)
    }

}