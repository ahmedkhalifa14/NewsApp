package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.model.Article

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.API.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.API.searchForNews(searchQuery, pageNumber)

    suspend fun updateOrInsert(article: Article) = db.articleDao().updateOrInsertArticle(article)
    fun getSavedNews() = db.articleDao().getAllArticle()
    suspend fun deleteArticle(article: Article) = db.articleDao().deleteArticle(article)

}