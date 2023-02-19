package com.yatik.newsworld.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yatik.newsworld.utils.TestConstants.Companion.EMPTY_SUCCESS_RESPONSE
import com.yatik.newsworld.utils.TestConstants.Companion.ERROR_RESPONSE
import com.yatik.newsworld.utils.TestConstants.Companion.ERROR_RESPONSE_CODE
import com.yatik.newsworld.utils.TestConstants.Companion.SUCCESS_BODY
import com.yatik.newsworld.utils.TestConstants.Companion.SUCCESS_RESPONSE
import com.yatik.newsworld.utils.TestConstants.Companion.SUCCESS_RESPONSE_CODE
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class NewsApiTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: NewsAPI

    private fun enqueueNewsResponse(responseBody: String, responseCode: Int) {
        val mockResponse = MockResponse()
            .setBody(responseBody)
            .setResponseCode(responseCode)

        mockWebServer.enqueue(mockResponse)
    }

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NewsAPI::class.java)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun `api returns empty breaking news list for empty responseBody`() = runTest {

        enqueueNewsResponse(EMPTY_SUCCESS_RESPONSE, SUCCESS_RESPONSE_CODE)

        val response = api.getBreakingNews()
        mockWebServer.takeRequest()

        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()!!.articles.isEmpty()).isTrue()

    }

    @Test
    fun `api returns breaking news list with 1 article`() = runTest {

        enqueueNewsResponse(SUCCESS_RESPONSE, SUCCESS_RESPONSE_CODE)

        val response = api.getBreakingNews()
        mockWebServer.takeRequest()

        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()!!.articles.size).isEqualTo(1)
        assertThat(response.body()!!.articles[0]).isEqualTo(SUCCESS_BODY)

    }

    @Test
    fun `api returns error breaking news for responseCode 404`() = runTest {

        enqueueNewsResponse(ERROR_RESPONSE, ERROR_RESPONSE_CODE)

        val response = api.getBreakingNews()
        mockWebServer.takeRequest()

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(ERROR_RESPONSE_CODE)

    }

    @Test
    fun `api returns empty search news list for empty responseBody`() = runTest {

        enqueueNewsResponse(EMPTY_SUCCESS_RESPONSE, SUCCESS_RESPONSE_CODE)

        val response = api.searchNews("testing")
        mockWebServer.takeRequest()

        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()!!.articles.isEmpty()).isTrue()

    }

    @Test
    fun `api returns search news list with 1 article`() = runTest {

        enqueueNewsResponse(SUCCESS_RESPONSE, SUCCESS_RESPONSE_CODE)

        val response = api.searchNews("testing")
        mockWebServer.takeRequest()

        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()!!.articles.size).isEqualTo(1)

    }

    @Test
    fun `api returns error search news for responseCode 404`() = runTest {

        enqueueNewsResponse(ERROR_RESPONSE, ERROR_RESPONSE_CODE)

        val response = api.searchNews("testing")
        mockWebServer.takeRequest()

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(404)

    }

}