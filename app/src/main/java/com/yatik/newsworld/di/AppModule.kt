package com.yatik.newsworld.di

import android.content.Context
import androidx.room.Room
import com.yatik.newsworld.database.ArticleDao
import com.yatik.newsworld.database.ArticleDatabase
import com.yatik.newsworld.network.NewsAPI
import com.yatik.newsworld.utils.ConnectivityHelper
import com.yatik.newsworld.utils.DefaultConnectivityHelper
import com.yatik.newsworld.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(
        @ApplicationContext appContext: Context
    ): ArticleDatabase = Room.databaseBuilder(
        appContext,
        ArticleDatabase::class.java,
        "article_database"
    ).build()

    @Provides
    @Singleton
    fun provideArticleDao(
        database: ArticleDatabase
    ): ArticleDao = database.articleDao()

    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(
        retrofit: Retrofit
    ): NewsAPI =
        retrofit.create(NewsAPI::class.java)

    @Provides
    fun provideConnectivityHelper(
        @ApplicationContext appContext: Context
    ): ConnectivityHelper = DefaultConnectivityHelper(appContext)

}