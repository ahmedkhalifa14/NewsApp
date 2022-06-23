package com.example.newsapp.database

import android.content.Context
import androidx.room.*
import com.example.newsapp.model.Article

@Database(
        entities = [Article::class],
        version = 3,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase:RoomDatabase() {
    abstract fun articleDao(): ArticleDAO
    companion object{
        @Volatile
        private var articleInstance: ArticleDatabase?=null
        private val LOCK = Any()
        operator fun invoke(context: Context)= articleInstance ?: synchronized(LOCK){
            articleInstance ?: createDatabase(context).also{ articleInstance = it}
        }

        private fun createDatabase(context: Context)=
                Room.databaseBuilder(
                        context.applicationContext,
                        ArticleDatabase::class.java,
                        "articles_db"
                ).fallbackToDestructiveMigration()
                    .build()
    }
}