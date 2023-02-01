package com.graphicless.newsapp.database

import android.content.Context
import androidx.room.*
import com.graphicless.newsapp.model.FavoriteArticle
import com.graphicless.newsapp.model.LocalNews
import com.graphicless.newsapp.utils.Converters

@Database(entities = [LocalNews::class, FavoriteArticle::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun getDao(): NewsDao

    companion object {

        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getDatabase(context: Context): NewsDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}