package com.graphicless.newsapp.model

import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "news_table",primaryKeys = ["url"])
data class LocalNews(
    @Embedded
    val article: Article,
    val category: String,
    val favorite: Boolean
)