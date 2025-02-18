package ink.metoo.auto.fishpi.job

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.ArticleCall
import org.quartz.Job
import org.quartz.JobExecutionContext

class AutoLikeArticleJob : Job {

    override fun execute(context: JobExecutionContext?) {
        // 支持或反对文章	20 积分	每天2次	3%
        val maxLike = 2
        var like = 0
        var p = 1
        var maxP = 10
        // 重试次数 预防无限循环
        var retryCount = 5
        while (maxLike > like && retryCount > 0 && p <= maxP) {
            try {
                ArticleCall.getArticles(p = p, size = 2).data?.let { data ->
                    maxP = data.pagination.paginationPageCount
                    data.articles.forEach { at ->
                        ArticleCall.getArticle(at.oId).data?.article?.let { ad ->
                            if (ad["articleVote"].asInt == -1) {
                                val result = ArticleCall.likeArticle(at.oId)
                                if (result.code != 0) {
                                    return@forEach
                                }
                                when (result.type) {
                                    0 -> /* 之前点赞过再给点回来 */ ArticleCall.likeArticle(at.oId)
                                    -1 -> {
                                        Log.debug("like article ${at.oId} ${at.articleTitle}")
                                        like++
                                    }
                                }
                            }
                        }

                    }
                }
                retryCount = 5
            } catch (e: Exception) {
                retryCount--
                Log.error("阅读文章发生错误", e)
            }
            p++
        }
    }

}