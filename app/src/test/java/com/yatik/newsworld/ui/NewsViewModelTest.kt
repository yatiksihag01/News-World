package com.yatik.newsworld.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yatik.newsworld.getOrAwaitValueTest
import com.yatik.newsworld.repository.FakeNewsRepository
import com.yatik.newsworld.utils.FakeConnectivityHelper
import com.yatik.newsworld.utils.TestConstants.Companion.SUCCESS_BODY
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NewsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var connectToInternet = true
    private var isServerUp = true

    private val fakeConnectivityHelper: FakeConnectivityHelper by lazy {
        FakeConnectivityHelper(connectToInternet)
    }
    private val viewModel: NewsViewModel by lazy {
        NewsViewModel(FakeNewsRepository(isServerUp), fakeConnectivityHelper)
    }

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `breaking news response if connected to internet`() = runTest {
        connectToInternet = true
        isServerUp = true

        viewModel.getBreakingNews("in")
        val newsResource = viewModel.breakingNews.getOrAwaitValueTest()

        assertThat(newsResource.data!!.status).isEqualTo("ok")
        assertThat(newsResource.data!!.totalResults).isEqualTo(1)
        assertThat(newsResource.data!!.articles).contains(SUCCESS_BODY)

    }

    @Test
    fun `error breaking news response when not connected to internet`() = runTest {
        connectToInternet = false
        isServerUp = true

        viewModel.getBreakingNews("in")
        val newsResource = viewModel.breakingNews.getOrAwaitValueTest()

        assertThat(newsResource.message).isEqualTo("No internet connection")
        assertThat(newsResource.data).isEqualTo(null)

    }

    @Test
    fun `error breaking news response when server not responding`() = runTest {

        connectToInternet = true
        isServerUp = false

        viewModel.getBreakingNews("in")
        val newsResource = viewModel.breakingNews.getOrAwaitValueTest()

        assertThat(newsResource.message).isEqualTo("Response.error()")

    }

    @Test
    fun `search news response if connected to internet`() = runTest {
        connectToInternet = true
        isServerUp = true

        viewModel.getBreakingNews("in")
        val newsResource = viewModel.breakingNews.getOrAwaitValueTest()

        assertThat(newsResource.data!!.status).isEqualTo("ok")
        assertThat(newsResource.data!!.totalResults).isEqualTo(1)
        assertThat(newsResource.data!!.articles).contains(SUCCESS_BODY)

    }

    @Test
    fun `error search news response when not connected to internet`() = runTest {
        connectToInternet = false
        isServerUp = true

        viewModel.getBreakingNews("in")
        val newsResource = viewModel.breakingNews.getOrAwaitValueTest()

        assertThat(newsResource.message).isEqualTo("No internet connection")
        assertThat(newsResource.data).isEqualTo(null)

    }

    @Test
    fun `error search news response when server not responding`() = runTest {

        connectToInternet = true
        isServerUp = false

        viewModel.getBreakingNews("in")
        val newsResource = viewModel.breakingNews.getOrAwaitValueTest()

        assertThat(newsResource.message).isEqualTo("Response.error()")

    }

    @Test
    fun `test insertion and verification in database`() {

        viewModel.saveArticle(SUCCESS_BODY)
        val insertedArticleList = viewModel.savedNews.getOrAwaitValueTest()
        assertThat(insertedArticleList).contains(SUCCESS_BODY)

    }

    @Test
    fun `test deletion and verification in database`() {

        viewModel.saveArticle(SUCCESS_BODY)
        viewModel.saveArticle(SUCCESS_BODY)
        val insertedArticleList1 = viewModel.savedNews.getOrAwaitValueTest()
        assertThat(insertedArticleList1.size).isEqualTo(2)

        viewModel.deleteArticle(SUCCESS_BODY)
        val insertedArticleList2 = viewModel.savedNews.getOrAwaitValueTest()
        assertThat(insertedArticleList2.size).isEqualTo(1)

    }

}