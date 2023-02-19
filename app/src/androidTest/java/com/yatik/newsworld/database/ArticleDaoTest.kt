package com.yatik.newsworld.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.yatik.newsworld.utils.TestConstants.Companion.EXPECTED_SAMPLE_ARTICLE_1
import com.yatik.newsworld.utils.TestConstants.Companion.EXPECTED_SAMPLE_ARTICLE_2
import com.yatik.newsworld.utils.TestConstants.Companion.SAMPLE_ARTICLE_1
import com.yatik.newsworld.utils.TestConstants.Companion.SAMPLE_ARTICLE_2
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class ArticleDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ArticleDatabase
    private lateinit var dao: ArticleDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.articleDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertArticle() = runTest {

        dao.upsert(SAMPLE_ARTICLE_1)

        launch {
            delay(1000)
            dao.upsert(SAMPLE_ARTICLE_2)
        }

        dao.getAllArticles().test {

            val articleList1 = awaitItem()
            assertThat(articleList1.size).isEqualTo(1)
            assertThat(articleList1).contains(EXPECTED_SAMPLE_ARTICLE_1)

            val articleList2 = awaitItem()
            assertThat(articleList2.size).isEqualTo(2)
            assertThat(articleList2).contains(EXPECTED_SAMPLE_ARTICLE_2)
            cancel()
        }

    }

    @Test
    fun deleteArticle() = runTest {

        dao.upsert(SAMPLE_ARTICLE_1)
        dao.upsert(SAMPLE_ARTICLE_2)

        dao.getAllArticles().test {

            val articleList = awaitItem()
            dao.deleteArticle(articleList[0])
            val articleList2 = awaitItem()

            assertThat(articleList2.size).isEqualTo(1)
            assertThat(articleList2).doesNotContain(EXPECTED_SAMPLE_ARTICLE_1)
            assertThat(articleList2).contains(EXPECTED_SAMPLE_ARTICLE_2)
            cancel()
        }

    }
    
}