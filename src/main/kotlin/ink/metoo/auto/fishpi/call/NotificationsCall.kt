package ink.metoo.auto.fishpi.call

import ink.metoo.auto.fishpi.ClientCaches

object NotificationsCall {

    class MakeArticleReadBody: AbstractBaseBody() {
        var articleId: String = ""
        var commentIds: String = ""
    }

    fun makeArticleRead(articleId: String, commentIds: String) = Requests.sendJsonRequest<BaseResult>("/notifications/make-read", MakeArticleReadBody().let {
        it.articleId = articleId
        it.commentIds = commentIds
        it.apiKey = ClientCaches.apiKey
        it
    })

}