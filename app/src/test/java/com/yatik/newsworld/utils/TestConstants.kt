package com.yatik.newsworld.utils

import com.yatik.newsworld.models.Article
import com.yatik.newsworld.models.Source

class TestConstants {

    companion object {

        const val SUCCESS_RESPONSE = "{\"status\":\"ok\",\"totalResults\":1,\"articles\":[{\"source\":{\"id\":\"123\",\"name\":\"NewsWorld\"}," +
                "\"author\":\"authorName\",\"title\":\"titleName\",\"description\":\"descriptionName\"," +
                "\"urlToImage\":\"https://fakeurl.com\"," +
                "\"publishedAt\":\"2023-02-16T10:30:00Z\"," +
                "\"content\":\"contentName\"}]}"

        const val ERROR_RESPONSE = "{\"status\":\"error\",\"code\":\"404\",\"message\":\"Server down\"}"

        const val EMPTY_SUCCESS_RESPONSE = "{\"status\":\"ok\",\"totalResults\":0,\"articles\":[]}"

        const val SUCCESS_RESPONSE_CODE = 200

        const val ERROR_RESPONSE_CODE = 404

        val SUCCESS_BODY = Article(null, "authorName", "contentName",
            "descriptionName", "2023-02-16T10:30:00Z",
            Source("123", "NewsWorld"),
            "titleName", null, "https://fakeurl.com"
        )

    }


}