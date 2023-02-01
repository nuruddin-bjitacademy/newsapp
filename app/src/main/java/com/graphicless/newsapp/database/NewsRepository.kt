package com.graphicless.newsapp.database

import androidx.lifecycle.LiveData
import com.graphicless.newsapp.model.FavoriteArticle
import com.graphicless.newsapp.model.LocalNews
import com.graphicless.newsapp.utils.AppConstants
import kotlinx.coroutines.flow.Flow

class NewsRepository(private val newsDao: NewsDao) {

    val getAllFavoriteNews: LiveData<List<FavoriteArticle>> = newsDao.getAllFavoriteNews()
    val getAllTopNews: LiveData<List<LocalNews>> = newsDao.getAllNews(AppConstants.COUNTRY)
    val getAllBusinessNews: LiveData<List<LocalNews>> = newsDao.getAllNews("business")
    val getAllEntertainmentNews: LiveData<List<LocalNews>> = newsDao.getAllNews("entertainment")
    val getAllGeneralNews: LiveData<List<LocalNews>> = newsDao.getAllNews("general")
    val getAllHealthNews: LiveData<List<LocalNews>> = newsDao.getAllNews("health")
    val getAllScienceNews: LiveData<List<LocalNews>> = newsDao.getAllNews("science")
    val getAllSportsNews: LiveData<List<LocalNews>> = newsDao.getAllNews("sports")
    val getAllTechnologyNews: LiveData<List<LocalNews>> = newsDao.getAllNews("technology")

    suspend fun insertFavoriteNews(favoriteArticle: FavoriteArticle){
        newsDao.insertFavoriteNews(favoriteArticle)
    }

    suspend fun removeFavoriteNews(favoriteArticle: FavoriteArticle){
        newsDao.removeFavoriteNews(favoriteArticle)
    }

    suspend fun removeNewsByCategory(category: String){
        newsDao.removeNewsByCategory(category)
    }

    suspend fun insertNews(localNews: LocalNews){
        newsDao.insertNews(localNews)
    }

    suspend fun updateNews(localNews: LocalNews){
        newsDao.updateNews(localNews)
    }

    fun searchFromCategory(category: String, searchQuery: String): Flow<List<LocalNews>> {
        return newsDao.searchFromCategory(category, searchQuery)
    }

    fun searchFromCategoryTitle(category: String, searchQuery: String): Flow<List<LocalNews>> {
        return newsDao.searchFromCategoryTitle(category, searchQuery)
    }

    fun searchFromCategoryContent(category: String, searchQuery: String): Flow<List<LocalNews>> {
        return newsDao.searchFromCategoryContent(category, searchQuery)
    }

    fun searchFromCategoryDescription(category: String, searchQuery: String): Flow<List<LocalNews>> {
        return newsDao.searchFromCategoryDescription(category, searchQuery)
    }

    fun searchFromCategoryTitleAndContent(category: String, searchQuery: String): Flow<List<LocalNews>> {
        return newsDao.searchFromCategoryTitleAndContent(category, searchQuery)
    }

    fun searchFromCategoryTitleAndDescription(category: String, searchQuery: String): Flow<List<LocalNews>> {
        return newsDao.searchFromCategoryTitleAndDescription(category, searchQuery)
    }

    fun searchFromCategoryContentAndDescription(category: String, searchQuery: String): Flow<List<LocalNews>> {
        return newsDao.searchFromCategoryContentAndDescription(category, searchQuery)
    }

    fun searchFromTopNews(category: String, searchQuery: String): Flow<List<LocalNews>>{
        return newsDao.searchFromTopNews(category, searchQuery)
    }
    fun searchFromTopNewsTitle(category: String, searchQuery: String): Flow<List<LocalNews>>{
        return newsDao.searchFromTopNewsTitle(category, searchQuery)
    }
    fun searchFromTopNewsDescription(category: String, searchQuery: String): Flow<List<LocalNews>>{
        return newsDao.searchFromTopNewsDescription(category, searchQuery)
    }
    fun searchFromTopNewsContent(category: String, searchQuery: String): Flow<List<LocalNews>>{
        return newsDao.searchFromTopNewsContent(category, searchQuery)
    }
    fun searchFromTopNewsTitleAndContent(category: String, searchQuery: String): Flow<List<LocalNews>>{
        return newsDao.searchFromTopNewsTitleAndContent(category, searchQuery)
    }
    fun searchFromTopNewsTitleAndDescription(category: String, searchQuery: String): Flow<List<LocalNews>>{
        return newsDao.searchFromTopNewsTitleAndDescription(category, searchQuery)
    }
    fun searchFromTopNewsContentAndDescription(category: String, searchQuery: String): Flow<List<LocalNews>>{
        return newsDao.searchFromTopNewsContentAndDescription(category, searchQuery)
    }

    fun searchFromFavorite(searchQuery: String): Flow<List<FavoriteArticle>>{
        return newsDao.searchFromFavorite(searchQuery)
    }

    fun searchFromFavoriteTitle(searchQuery: String): Flow<List<FavoriteArticle>>{
        return newsDao.searchFromFavoriteTitle(searchQuery)
    }

    fun searchFromFavoriteContent(searchQuery: String): Flow<List<FavoriteArticle>>{
        return newsDao.searchFromFavoriteContent(searchQuery)
    }

    fun searchFromFavoriteDescription(searchQuery: String): Flow<List<FavoriteArticle>>{
        return newsDao.searchFromFavoriteDescription(searchQuery)
    }

    fun searchFromFavoriteTitleAndContent(searchQuery: String): Flow<List<FavoriteArticle>>{
        return newsDao.searchFromFavoriteTitleAndContent(searchQuery)
    }

    fun searchFromFavoriteTitleAndDescription(searchQuery: String): Flow<List<FavoriteArticle>>{
        return newsDao.searchFromFavoriteTitleAndDescription(searchQuery)
    }

    fun searchFromFavoriteContentAndDescription(searchQuery: String): Flow<List<FavoriteArticle>>{
        return newsDao.searchFromFavoriteContentAndDescription(searchQuery)
    }

}