package com.yatik.newsworld.database

import androidx.room.*
import com.yatik.newsworld.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT * FROM articles_table")
    fun getAllArticles(): Flow<List<Article>>

}