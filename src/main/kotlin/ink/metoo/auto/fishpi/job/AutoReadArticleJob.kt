package ink.metoo.auto.fishpi.job

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.ArticleCall
import ink.metoo.auto.fishpi.call.NotificationsCall
import org.quartz.Job
import org.quartz.JobExecutionContext

class AutoReadArticleJob : Job {

    override fun execute(context: JobExecutionContext) {
        // 查看文章	5 积分	每天20篇	0.75%
        val maxRead = 20
        var read = 0
        var p = 1
        var maxP = 10
        // 重试次数 预防无限循环
        var retryCount = 5
        while (maxRead > read && retryCount > 0 && p <= maxP) {
            try {
                ArticleCall.getArticles(p = p, size = 5).data?.let { data ->
                    maxP = data.pagination.paginationPageCount
                    data.articles.forEach { at ->
                        ArticleCall.getArticle(at.oId).data?.article?.let { ad ->
                            val comments = ad
                                .getAsJsonArray("articleComments")
                                .map { it.asJsonObject["commentAuthorId"].asString }
                                .toString()
                            NotificationsCall.makeArticleRead(at.oId, comments)
                            Log.debug("read article ${at.oId} ${at.articleTitle}")
                            read ++
                        }
                    }
                }
                retryCount = 5
            } catch (e: Exception) {
                retryCount --
                Log.error("阅读文章发生错误", e)
            }
            p ++
        }
    }

}