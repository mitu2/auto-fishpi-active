package ink.metoo.auto.fishpi.call

import com.google.gson.JsonObject
import ink.metoo.auto.fishpi.ClientCaches

object ArticleCall {

    class ArticlesResult : AbstractBaseResult() {

        var data: Data? = null

        class Article {
            var articleShowInList: Int = 0
            var articleCreateTime: String = ""
            var articleAuthorId: String = ""
            var articleBadCnt: Int = 0
            var articleLatestCmtTime: String = ""
            var articleGoodCnt: Int = 0
            var articleQnAOfferPoint: Int = 0
            var articleThumbnailURL: String = ""
            var articleStickRemains: Long = 0
            var timeAgo: String = ""
            var articleUpdateTimeStr: String = ""
            var articleAuthorName: String = ""
            var articleType: Int = 0
            var offered: Boolean = false
            var articleCreateTimeStr: String = ""
            var articleViewCount: Int = 0
            var articleAuthorThumbnailURL20: String = ""
            var articleWatchCnt: Int = 0
            var articlePreviewContent: String = ""
            var articleTitleEmoj: String = ""
            var articleTitleEmojUnicode: String = ""
            var articleAuthorThumbnailURL48: String = ""
            var articleCommentCount: Int = 0
            var articleCollectCnt: Int = 0
            var articleTitle: String = ""
            var articleLatestCmterName: String = ""
            var oId: String = ""
            var cmtTimeAgo: String = ""
            var articleStick: Long = 0
            var articleTagObjs: List<ArticleTag> = listOf()
            var articleLatestCmtTimeStr: String = ""
            var articleAnonymous: Int = 0
            var articleViewCntDisplayFormat: String = ""
            var articleThankCnt: Int = 0
            var articleUpdateTime: String = ""
            var articleStatus: Int = 0
            var articleHeat: Int = 0
            var articlePerfect: Int = 0
            var articleAuthorThumbnailURL210: String = ""
            var articlePermalink: String = ""
            var articleAuthor: Author = Author()
        }

        class ArticleTag {
            var tagShowSideAd: Int = 0
            var tagIconPath: String = ""
            var tagStatus: Int = 0
            var tagBadCnt: Int = 0
            var tagRandomDouble: Double = 0.0
            var tagTitle: String = ""
            var oId: String = ""
            var tagURI: String = ""
            var tagAd: String = ""
            var tagGoodCnt: Int = 0
            var tagCSS: String = ""
            var tagCommentCount: Int = 0
            var tagFollowerCount: Int = 0
            var tagSeoTitle: String = ""
            var tagLinkCount: Int = 0
            var tagSeoDesc: String = ""
            var tagReferenceCount: Int = 0
            var tagSeoKeywords: String = ""
            var tagDescription: String = ""
        }

        class Author {
            var userOnlineFlag: Boolean = false
            var onlineMinute: Int = 0
            var userPointStatus: Int = 0
            var userFollowerStatus: Int = 0
            var userGuideStep: Int = 0
            var userOnlineStatus: Int = 0
            var userCurrentCheckinStreakStart: Int = 0
            var chatRoomPictureStatus: Int = 0
            var userTags: String = ""
            var userCommentStatus: Int = 0
            var userTimezone: String = ""
            var userURL: String = ""
            var userForwardPageStatus: Int = 0
            var userUAStatus: Int = 0
            var userIndexRedirectURL: String = ""
            var userLatestArticleTime: Long = 0
            var userTagCount: Int = 0
            var userNickname: String = ""
            var userListViewMode: Int = 0
            var userLongestCheckinStreak: Int = 0
            var userAvatarType: Int = 0
            var userSubMailSendTime: Long = 0
            var userUpdateTime: Long = 0
            var userSubMailStatus: Int = 0
            var userJoinPointRank: Int = 0
            var userLatestLoginTime: Long = 0
            var userAppRole: Int = 0
            var userAvatarViewMode: Int = 0
            var userStatus: Int = 0
            var userLongestCheckinStreakEnd: Int = 0
            var userWatchingArticleStatus: Int = 0
            var userLatestCmtTime: Long = 0
            var userProvince: String = ""
            var userCurrentCheckinStreak: Int = 0
            var userNo: Int = 0
            var userAvatarURL: String = ""
            var userFollowingTagStatus: Int = 0
            var userLanguage: String = ""
            var userJoinUsedPointRank: Int = 0
            var userCurrentCheckinStreakEnd: Int = 0
            var userFollowingArticleStatus: Int = 0
            var userKeyboardShortcutsStatus: Int = 0
            var userReplyWatchArticleStatus: Int = 0
            var userCommentViewMode: Int = 0
            var userBreezemoonStatus: Int = 0
            var userCheckinTime: Long = 0
            var userUsedPoint: Int = 0
            var userArticleStatus: Int = 0
            var userPoint: Int = 0
            var userCommentCount: Int = 0
            var userIntro: String = ""
            var userMobileSkin: String = ""
            var userListPageSize: Int = 0
            var oId: String = ""
            var userName: String = ""
            var userGeoStatus: Int = 0
            var userLongestCheckinStreakStart: Int = 0
            var userSkin: String = ""
            var userNotifyStatus: Int = 0
            var userFollowingUserStatus: Int = 0
            var userArticleCount: Int = 0
            var mbti: String = ""
            var userRole: String = ""
        }

        class Data {
            var articles: List<Article> = listOf()
            var pagination: Pagination = Pagination()
        }
    }

    fun getArticles(typePath: String? = null, p: Int = 1, size: Int = 1) = Requests.sendGetRequest<ArticlesResult>(
        "/api/articles/recent${typePath?.let { "/$it" } ?: ""}",
        arrayOf(
            "apiKey" to ClientCaches.apiKey,
            "p" to p.toString(),
            "size" to size.toString()
        )
    )

    class ArticleResult: AbstractBaseResult() {

        var data: Data? = null

        class Data {
            var article: JsonObject? = null
            var pagination: Pagination = Pagination()
        }

    }

    fun getArticle(aid: String) = Requests.sendGetRequest<ArticleResult>(
        "/api/article/${aid}",
        arrayOf(
            "apiKey" to ClientCaches.apiKey,
        )
    )

}

// ai 生成的类


