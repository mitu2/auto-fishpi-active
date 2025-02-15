package ink.metoo.auto.fishpi.call

import org.junit.Test


class ArticleCallTest {

    @Test
    fun testCase() {
        println(ArticleCall.thankArticle("1739526653048"))
//        val result = ArticleCall.getArticles()
//        val oId = result.data?.articles?.get(0)?.oId
//        ArticleCall.getArticle(oId!!).data?.article?.let { at ->
//            val comments = at
//                .getAsJsonArray("articleComments")
//                .map { it.asJsonObject["commentAuthorId"].asString }
//                .toList()
//            comments
//        }
    }

}