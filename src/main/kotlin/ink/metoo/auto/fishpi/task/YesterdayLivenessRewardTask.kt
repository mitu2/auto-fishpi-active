package ink.metoo.auto.fishpi.task

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.UserCall
import java.util.*
import java.util.concurrent.TimeUnit

class YesterdayLivenessRewardTask : Runnable {

    private val timer = Timer()

    override fun run() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                val yesterdayLivenessReward = UserCall.yesterdayLivenessReward()
                when (UserCall.yesterdayLivenessReward()) {
                    null -> Log.error("获取昨日活跃度奖励失败")
                    -1 -> Log.info("已领取过昨日活跃度奖励")
                    else -> Log.info("获取昨日活跃度奖励成功，获得${yesterdayLivenessReward}积分")
                }
            }
        }, TimeUnit.SECONDS.toMillis(5L))
    }

}