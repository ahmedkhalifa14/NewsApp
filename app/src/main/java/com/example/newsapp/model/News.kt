package com.example.newsapp.model

import java.io.Serializable


data class News(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
): Serializable