package ink.metoo.auto.fishpi.call

import ink.metoo.auto.fishpi.ClientCaches

object CommentCall {

    class CommentBody(
        var articleId: String,
        var commentContent: String,
        var commentAnonymous: Boolean = false,
        var commentVisible: Boolean = false,
        var commentOriginalCommentId: String? = null
    ) : AbstractBaseBody()

    fun comment(
        articleId: String,
        commentContent: String,
        commentAnonymous: Boolean = false,
        commentVisible: Boolean = false,
        commentOriginalCommentId: String? = null
    ) = Requests.sendJsonRequest<BaseResult>(path = "/comment", body = CommentBody(
        articleId = articleId,
        commentContent = commentContent,
        commentAnonymous = commentAnonymous,
        commentVisible = commentVisible,
        commentOriginalCommentId = commentOriginalCommentId
    ).let {
        it.apiKey = ClientCaches.apiKey
        it
    })

}