package ink.metoo.auto.fishpi.job

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.Settings
import ink.metoo.auto.fishpi.call.ArticleCall
import ink.metoo.auto.fishpi.call.CommentCall
import org.quartz.Job
import org.quartz.JobExecutionContext


class AutoCommentArticleJob : Job {

    override fun execute(context: JobExecutionContext) {
        ArticleCall.getArticlesByTag("新人报到").data?.articles?.forEach { article ->
            ArticleCall.getArticle(article.oId).data?.article?.let { ad ->
                if (!ad["articleCommentable"].asBoolean) {
                    return
                }
                val myComment = ad["articleComments"].asJsonArray.find { comment ->
                    comment.asJsonObject["commentAuthorName"].asString == Settings.fishpiClient.username
                }
                if (myComment != null) {
                    return
                }
                CommentCall.comment(article.oId, Settings.community.newcomerWelcomeMessages.random())
                Log.debug("comment article ${article.oId} ${article.articleTitle}")
            }
        }
    }

}