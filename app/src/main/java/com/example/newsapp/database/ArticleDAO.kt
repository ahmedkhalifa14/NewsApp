package com.example.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.model.Article

@Dao
abstract interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsertArticle(article: Article):Long
    @Query("SELECT * FROM articles")
    fun getAllArticle(): LiveData<List<Article>>
    @Delete
    suspend fun deleteArticle(article: Article)
}