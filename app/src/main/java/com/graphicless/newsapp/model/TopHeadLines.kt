package com.graphicless.newsapp.model

data class TopHeadLines(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)