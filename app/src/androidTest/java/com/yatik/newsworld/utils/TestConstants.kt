package com.yatik.newsworld.utils

import com.yatik.newsworld.models.Article
import com.yatik.newsworld.models.Source

class TestConstants {

    companion object {

        val SAMPLE_ARTICLE_1 = Article(null, "authorName", "contentName",
            "descriptionName", "2023-02-16T10:30:00Z",
            Source("123", "NewsWorld"),
            "titleName", null, "https://fakeurl.com")

        val SAMPLE_ARTICLE_2 = Article(null, "authorName2", "contentName2",
            "descriptionName2", "2023-02-16T10:30:00Z",
            Source("123", "NewsWorld"),
            "titleName2", null, "https://fakeurl2.com")

        val EXPECTED_SAMPLE_ARTICLE_1 = Article(1, "authorName", "contentName",
            "descriptionName", "2023-02-16T10:30:00Z",
            Source("NewsWorld", "NewsWorld"),
            "titleName", null, "https://fakeurl.com")

        val EXPECTED_SAMPLE_ARTICLE_2 = Article(2, "authorName2", "contentName2",
            "descriptionName2", "2023-02-16T10:30:00Z",
            Source("NewsWorld", "NewsWorld"),
            "titleName2", null, "https://fakeurl2.com")
    }

}