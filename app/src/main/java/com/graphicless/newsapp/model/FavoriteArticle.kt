package com.graphicless.newsapp.model

import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "favorite_news_table",primaryKeys = ["url"])
data class FavoriteArticle(
    @Embedded
    val localNews: LocalNews
)