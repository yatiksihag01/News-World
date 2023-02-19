package com.yatik.newsworld.repository

import com.yatik.newsworld.models.Article
import com.yatik.newsworld.models.NewsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRepository {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse>
    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>
    suspend fun upsert(article: Article)
    suspend fun deleteArticle(article: Article)
    fun getAllArticles(): Flow<List<Article>>

}