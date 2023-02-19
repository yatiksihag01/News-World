package com.yatik.newsworld.repository

import androidx.annotation.WorkerThread
import com.yatik.newsworld.database.ArticleDao
import com.yatik.newsworld.models.Article
import com.yatik.newsworld.network.NewsAPI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultNewsRepository @Inject constructor(
    private val articleDao: ArticleDao,
    private val api: NewsAPI
    ) : NewsRepository {

    @WorkerThread
    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    @WorkerThread
    override suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        api.searchNews(searchQuery, pageNumber)

    @WorkerThread
    override suspend fun upsert(article: Article) =
        articleDao.upsert(article)

    @WorkerThread
    override suspend fun deleteArticle(article: Article) =
        articleDao.deleteArticle(article)

    override fun getAllArticles(): Flow<List<Article>> =
        articleDao.getAllArticles()

}