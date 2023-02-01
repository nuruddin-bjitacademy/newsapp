package com.graphicless.newsapp.utils

import com.graphicless.newsapp.BuildConfig

class AppConstants {



    companion object{
        const val CATEGORY_TAB_NUMBER = "category_tab_number"
        const val BASE_URL = "https://newsapi.org/v2/"
        const val API_KEY  = BuildConfig.apiKey
        const val ARTICLE = "article"
        const val FAVORITE = "favorite"
        const val CATEGORY = "category"
        const val SOURCE = "source"
        const val FROM = "cameFrom"
        var COUNTRY = "us"
        const val BUSINESS = "business"
        const val ENTERTAINMENT = "entertainment"
        const val GENERAL = "general"
        const val HEALTH = "health"
        const val SCIENCE = "science"
        const val SPORTS = "sports"
        const val TECHNOLOGY = "technology"
        const val NO_INTERNET_MESSAGE = "Internet connection is unavailable"
    }

}