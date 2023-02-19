package com.yatik.newsworld.di

import com.yatik.newsworld.repository.DefaultNewsRepository
import com.yatik.newsworld.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        defaultNewsRepository: DefaultNewsRepository
    ): NewsRepository

}