package com.graphicless.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.graphicless.newsapp.model.FavoriteArticle
import com.graphicless.newsapp.model.LocalNews
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = IGNORE)
    suspend fun insertFavoriteNews(favoriteArticle: FavoriteArticle)

    @Delete
    suspend fun removeFavoriteNews(favoriteArticle: FavoriteArticle)

    @Query("DELETE FROM news_table WHERE category = :category")
    suspend fun removeNewsByCategory(category: String)

    @Insert(onConflict = IGNORE)
    suspend fun insertNews(localNews: LocalNews)

    @Update
    suspend fun updateNews(localNews: LocalNews)

    @Query("SELECT COUNT(*) FROM news_table WHERE url = :url")
    fun count(url: String): Int

    suspend fun insertIfNotExist(localNews: LocalNews): Boolean {
        val count = count(localNews.article.url)
        if (count == 0) {
            insertNews(localNews)
            return true
        }
        return false
    }

    @Query("SELECT * FROM favorite_news_table")
    fun getAllFavoriteNews(): LiveData<List<FavoriteArticle>>

    @Query("SELECT * FROM news_table")
    fun getAllCategoryNews(): LiveData<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category")
    fun getAllNews(category: String): LiveData<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND (title LIKE :searchQuery OR description LIKE :searchQuery OR content LIKE :searchQuery)")
    fun searchFromCategory(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND (title LIKE :searchQuery)")
    fun searchFromCategoryTitle(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND (content LIKE :searchQuery)")
    fun searchFromCategoryContent(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND (description LIKE :searchQuery)")
    fun searchFromCategoryDescription(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND (title LIKE :searchQuery OR content LIKE :searchQuery)")
    fun searchFromCategoryTitleAndContent(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND (title LIKE :searchQuery OR description LIKE :searchQuery)")
    fun searchFromCategoryTitleAndDescription(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND (description LIKE :searchQuery OR content LIKE :searchQuery)")
    fun searchFromCategoryContentAndDescription(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND (title LIKE :searchQuery OR description LIKE :searchQuery OR content LIKE :searchQuery)")
    fun searchFromTopNews(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND ( title LIKE :searchQuery)")
    fun searchFromTopNewsTitle(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND ( description LIKE :searchQuery)")
    fun searchFromTopNewsDescription(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND ( content LIKE :searchQuery)")
    fun searchFromTopNewsContent(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND ( title LIKE :searchQuery OR content LIKE :searchQuery)")
    fun searchFromTopNewsTitleAndContent(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND ( title LIKE :searchQuery OR description LIKE :searchQuery)")
    fun searchFromTopNewsTitleAndDescription(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM news_table WHERE category = :category AND ( description LIKE :searchQuery OR content LIKE :searchQuery)")
    fun searchFromTopNewsContentAndDescription(category: String, searchQuery: String): Flow<List<LocalNews>>

    @Query("SELECT * FROM favorite_news_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery OR content LIKE :searchQuery")
    fun searchFromFavorite(searchQuery: String): Flow<List<FavoriteArticle>>

    @Query("SELECT * FROM favorite_news_table WHERE title LIKE :searchQuery")
    fun searchFromFavoriteTitle(searchQuery: String): Flow<List<FavoriteArticle>>

    @Query("SELECT * FROM favorite_news_table WHERE content LIKE :searchQuery")
    fun searchFromFavoriteContent(searchQuery: String): Flow<List<FavoriteArticle>>

    @Query("SELECT * FROM favorite_news_table WHERE description LIKE :searchQuery")
    fun searchFromFavoriteDescription(searchQuery: String): Flow<List<FavoriteArticle>>

    @Query("SELECT * FROM favorite_news_table WHERE title LIKE :searchQuery OR content LIKE :searchQuery")
    fun searchFromFavoriteTitleAndContent(searchQuery: String): Flow<List<FavoriteArticle>>

    @Query("SELECT * FROM favorite_news_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchFromFavoriteTitleAndDescription(searchQuery: String): Flow<List<FavoriteArticle>>

    @Query("SELECT * FROM favorite_news_table WHERE description LIKE :searchQuery OR content LIKE :searchQuery")
    fun searchFromFavoriteContentAndDescription(searchQuery: String): Flow<List<FavoriteArticle>>

}