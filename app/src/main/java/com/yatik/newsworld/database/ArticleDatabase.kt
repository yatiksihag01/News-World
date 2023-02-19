package com.yatik.newsworld.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yatik.newsworld.models.Article

@Database(
    version = 1,
    entities = [Article::class]
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

}