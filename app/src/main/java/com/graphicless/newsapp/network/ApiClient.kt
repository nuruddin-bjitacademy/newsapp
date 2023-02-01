package com.graphicless.newsapp.network

import com.graphicless.newsapp.model.TopHeadLines
import com.graphicless.newsapp.utils.AppConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit: Retrofit by lazy {
    Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(
        MoshiConverterFactory.create(
            moshi
        )
    ).build()
}

interface ApiService {


    @GET("top-headlines")
    suspend fun fetchNewsByCategory(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = AppConstants.API_KEY
    ): TopHeadLines
    @GET("top-headlines")
    suspend fun fetchTopNewsByCountry(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = AppConstants.API_KEY
    ): TopHeadLines
}

object NewsApi {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}