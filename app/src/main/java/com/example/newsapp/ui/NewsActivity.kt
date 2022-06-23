package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.database.ArticleDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView


class NewsActivity : AppCompatActivity() {

    val newsRepository by lazy {NewsRepository(ArticleDatabase(this))}
    val viewModelFactory by lazy { NewsViewModelFactory(application,newsRepository) }
    val viewModel: NewsViewModel by lazy {  ViewModelProvider(this, viewModelFactory)
        .get(NewsViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)


       // bottom_nav_view.setupWithNavController(fragment.findNavController())
//
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        val navController=findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)
//        val appBarConfiguration= AppBarConfiguration(setOf(R.id.breakingNewsFragment,R.id.savedNewsFragment,R.id.searchNewsFragment))
//        setupActionBarWithNavController(navController,appBarConfiguration)


    }
}


