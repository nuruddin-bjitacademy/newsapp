package com.graphicless.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.graphicless.newsapp.database.NewsDao
import com.graphicless.newsapp.database.NewsDatabase
import com.graphicless.newsapp.database.NewsRepository
import com.graphicless.newsapp.model.Article
import com.graphicless.newsapp.model.FavoriteArticle
import com.graphicless.newsapp.model.LocalNews
import com.graphicless.newsapp.network.NewsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    val favoriteNews: LiveData<List<FavoriteArticle>>
    val topNews: LiveData<List<LocalNews>>
    val businessNews: LiveData<List<LocalNews>>
    val entertainmentNews: LiveData<List<LocalNews>>
    val generalNews: LiveData<List<LocalNews>>
    val healthNews: LiveData<List<LocalNews>>
    val scienceNews: LiveData<List<LocalNews>>
    val sportsNews: LiveData<List<LocalNews>>
    val technologyNews: LiveData<List<LocalNews>>

    private val repository: NewsRepository
    private var newsDao: NewsDao

    init {
        newsDao = NewsDatabase.getDatabase(application).getDao()
        repository = NewsRepository(newsDao)

        favoriteNews = repository.getAllFavoriteNews
        topNews = repository.getAllTopNews
        businessNews = repository.getAllBusinessNews
        entertainmentNews = repository.getAllEntertainmentNews
        generalNews = repository.getAllGeneralNews
        healthNews = repository.getAllHealthNews
        scienceNews = repository.getAllScienceNews
        sportsNews = repository.getAllSportsNews
        technologyNews = repository.getAllTechnologyNews
    }


     /*val data = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 20
        ),
    ) {
        val dao = NewsDatabase.getDatabase(application).getDao()
        MainPagingSource(dao)
    }.flow.cachedIn(viewModelScope)*/



    fun insertFavoriteNews(favoriteArticle: FavoriteArticle) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoriteNews(favoriteArticle)
        }
    }

    fun removeFavoriteNews(favoriteArticle: FavoriteArticle){
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFavoriteNews(favoriteArticle)
        }
    }

    fun insertNews(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val articles: List<Article> =
                NewsApi.retrofitService.fetchNewsByCategory(category).articles
            for (article in articles) {
                repository.insertNews(LocalNews(article, category, false))
            }
        }
    }

    fun insertTopNews(country: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val articles: List<Article> =
                NewsApi.retrofitService.fetchTopNewsByCountry(country).articles
            for (article in articles) {
                repository.insertNews(LocalNews(article, country, false))
            }
        }
    }

    fun reInsertTopNews(country: String):Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            val articles: List<Article> =
                NewsApi.retrofitService.fetchTopNewsByCountry(country).articles
            if(articles.isNotEmpty()){
                repository.removeNewsByCategory(country)
                for (article in articles) {
                    repository.insertNews(LocalNews(article, country, false))
                }
            }
        }
        return true
    }

    fun reInsertNews(category: String):Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            val articles: List<Article> =
                NewsApi.retrofitService.fetchNewsByCategory(category).articles
            if(articles.isNotEmpty()){
                repository.removeNewsByCategory(category)
                for (article in articles) {
                    repository.insertNews(LocalNews(article, category, false))
                }
            }
        }
        return true
    }

    fun updateNews(localNews: LocalNews) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNews(localNews)
        }
    }

    fun searchFromCategory(category: String, searchQuery: String): LiveData<List<LocalNews>> {
        return repository.searchFromCategory(category, searchQuery).asLiveData()
    }

    fun searchFromCategoryTitle(category: String, searchQuery: String): LiveData<List<LocalNews>> {
        return repository.searchFromCategoryTitle(category, searchQuery).asLiveData()
    }

    fun searchFromCategoryContent(category: String, searchQuery: String): LiveData<List<LocalNews>> {
        return repository.searchFromCategoryContent(category, searchQuery).asLiveData()
    }

    fun searchFromCategoryDescription(category: String, searchQuery: String): LiveData<List<LocalNews>> {
        return repository.searchFromCategoryDescription(category, searchQuery).asLiveData()
    }

    fun searchFromCategoryTitleAndContent(category: String, searchQuery: String): LiveData<List<LocalNews>> {
        return repository.searchFromCategoryTitleAndContent(category, searchQuery).asLiveData()
    }

    fun searchFromCategoryTitleAndDescription(category: String, searchQuery: String): LiveData<List<LocalNews>> {
        return repository.searchFromCategoryTitleAndDescription(category, searchQuery).asLiveData()
    }

    fun searchFromCategoryContentAndDescription(category: String, searchQuery: String): LiveData<List<LocalNews>> {
        return repository.searchFromCategoryContentAndDescription(category, searchQuery).asLiveData()
    }

    fun searchFromTopNews(category: String, searchQuery: String): LiveData<List<LocalNews>>{
        return repository.searchFromTopNews(category, searchQuery).asLiveData()
    }

    fun searchFromTopNewsTitle(category: String, searchQuery: String): LiveData<List<LocalNews>>{
        return repository.searchFromTopNewsTitle(category, searchQuery).asLiveData()
    }

    fun searchFromTopNewsDescription(category: String, searchQuery: String): LiveData<List<LocalNews>>{
        return repository.searchFromTopNewsDescription(category, searchQuery).asLiveData()
    }

    fun searchFromTopNewsContent(category: String, searchQuery: String): LiveData<List<LocalNews>>{
        return repository.searchFromTopNewsContent(category, searchQuery).asLiveData()
    }

    fun searchFromTopNewsTitleAndContent(category: String, searchQuery: String): LiveData<List<LocalNews>>{
        return repository.searchFromTopNewsTitleAndContent(category, searchQuery).asLiveData()
    }

    fun searchFromTopNewsTitleAndDescription(category: String, searchQuery: String): LiveData<List<LocalNews>>{
        return repository.searchFromTopNewsTitleAndDescription(category, searchQuery).asLiveData()
    }

    fun searchFromTopNewsContentAndDescription(category: String, searchQuery: String): LiveData<List<LocalNews>>{
        return repository.searchFromTopNewsContentAndDescription(category, searchQuery).asLiveData()
    }

    fun searchFromFavorite(searchQuery: String): LiveData<List<FavoriteArticle>>{
        return repository.searchFromFavorite(searchQuery).asLiveData()
    }

    fun searchFromFavoriteTitle(searchQuery: String): LiveData<List<FavoriteArticle>>{
        return repository.searchFromFavoriteTitle(searchQuery).asLiveData()
    }

    fun searchFromFavoriteContent(searchQuery: String): LiveData<List<FavoriteArticle>>{
        return repository.searchFromFavoriteContent(searchQuery).asLiveData()
    }

    fun searchFromFavoriteDescription(searchQuery: String): LiveData<List<FavoriteArticle>>{
        return repository.searchFromFavoriteDescription(searchQuery).asLiveData()
    }

    fun searchFromFavoriteTitleAndContent(searchQuery: String): LiveData<List<FavoriteArticle>>{
        return repository.searchFromFavoriteTitleAndContent(searchQuery).asLiveData()
    }

    fun searchFromFavoriteTitleAndDescription(searchQuery: String): LiveData<List<FavoriteArticle>>{
        return repository.searchFromFavoriteTitleAndDescription(searchQuery).asLiveData()
    }

    fun searchFromFavoriteContentAndDescription(searchQuery: String): LiveData<List<FavoriteArticle>>{
        return repository.searchFromFavoriteContentAndDescription(searchQuery).asLiveData()
    }
}