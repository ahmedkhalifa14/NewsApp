package com.example.newsapp.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.NewsApplication
import com.example.newsapp.model.Article
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.model.News
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app: Application, private val newsRepository: NewsRepository) :
    AndroidViewModel(app) {
    val breakingNewsLiveData: MutableLiveData<Resource<News>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: News? = null


    val searchNewsLiveData: MutableLiveData<Resource<News>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: News? = null

    init {
        getBreakingNews("eg")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNewsCall(countryCode)
    }

    private fun handleBreakingNewsResponse(response: Response<News>): Resource<News> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())


    }


    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsCall(searchQuery)
    }


    private fun handleSearchNewsResponse(response: Response<News>): Resource<News> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())


    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.updateOrInsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()
    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun searchNewsCall(searchQuery: String) {
        searchNewsLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNewsLiveData.postValue(handleSearchNewsResponse(response))
            } else {
                searchNewsLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNewsLiveData.postValue(Resource.Error("Network failure"))
                else -> searchNewsLiveData.postValue(Resource.Error("Conversion Error"))

            }
        }
    }


    private suspend fun breakingNewsCall(countryCode: String) {
        breakingNewsLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNewsLiveData.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNewsLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNewsLiveData.postValue(Resource.Error("Network failure"))
                else -> breakingNewsLiveData.postValue(Resource.Error("Conversion Error"))

            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}