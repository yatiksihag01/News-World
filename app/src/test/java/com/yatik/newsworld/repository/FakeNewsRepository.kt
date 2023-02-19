package com.yatik.newsworld.repository

import com.yatik.newsworld.models.Article
import com.yatik.newsworld.models.NewsResponse
import com.yatik.newsworld.utils.TestConstants.Companion.ERROR_RESPONSE
import com.yatik.newsworld.utils.TestConstants.Companion.SUCCESS_RESPONSE
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeNewsRepository(
    private val isServerUp: Boolean
) : NewsRepository {

    private var _articlesList = mutableListOf<Article>()
    private val articlesList: List<Article>
        get() = _articlesList

    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Response<NewsResponse> {

        if (isServerUp) {
            val newsResponse = Gson().fromJson(SUCCESS_RESPONSE, NewsResponse::class.java)
            return Response.success(newsResponse)
        }
        val newsResponse = ERROR_RESPONSE.toResponseBody("application/json".toMediaTypeOrNull())
        return Response.error(404, newsResponse)

    }

    override suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {

        if (isServerUp) {
            val newsResponse = Gson().fromJson(SUCCESS_RESPONSE, NewsResponse::class.java)
            return Response.success(newsResponse)
        }
        val newsResponse = ERROR_RESPONSE.toResponseBody("application/json".toMediaTypeOrNull())
        return Response.error(404, newsResponse)

    }

    override suspend fun upsert(article: Article) {
        _articlesList.add(article)
    }

    override suspend fun deleteArticle(article: Article) {
        _articlesList.remove(article)
    }

    override fun getAllArticles(): Flow<List<Article>> {
        return flow {
            emit(articlesList)
        }
    }

}