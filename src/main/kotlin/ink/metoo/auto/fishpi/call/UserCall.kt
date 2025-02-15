package ink.metoo.auto.fishpi.call

import com.google.gson.annotations.SerializedName
import ink.metoo.auto.fishpi.ClientCaches
import ink.metoo.auto.fishpi.Settings
import org.apache.commons.codec.digest.DigestUtils

object UserCall {

    private data class GetKeyBody(val nameOrEmail: String, val userPassword: String, val mfaCode: String? = null)

    class GetKeyResult : AbstractBaseResult() {
        @SerializedName("Key")
        var key: String? = null
    }

    fun getKey() = Requests.sendJsonRequest<GetKeyResult>(
        "/api/getKey",
        body = GetKeyBody(
            Settings.fishpiClient.username,
            DigestUtils.md5Hex(Settings.fishpiClient.password),
            Settings.fishpiClient.mfaCode
        )
    )

    fun getUser() = Requests.sendGetRequest<BaseResult>("/api/user", arrayOf("apiKey" to ClientCaches.apiKey))

    class LivenessResult {
        var liveness: Double? = null
    }

    fun getLiveness() =
        Requests.sendGetRequest<LivenessResult>("/user/liveness", arrayOf("apiKey" to ClientCaches.apiKey)).liveness

    class YesterdayLivenessReward {
        var sum: Int? = null
    }

    /**
     * 领取昨日活跃奖励
     */
    fun yesterdayLivenessReward() = Requests.sendGetRequest<YesterdayLivenessReward>(
        "/activity/yesterday-liveness-reward-api",
        arrayOf("apiKey" to ClientCaches.apiKey)
    ).sum

}



