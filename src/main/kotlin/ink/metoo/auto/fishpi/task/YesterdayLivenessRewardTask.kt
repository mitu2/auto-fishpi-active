package ink.metoo.auto.fishpi.task

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.UserCall
import java.util.*
import java.util.concurrent.TimeUnit

object YesterdayLivenessRewardTask : Runnable {

    private val timer = Timer()

    class Task(private var retryCount: Int) : TimerTask() {

        private var currentRetryCount = 0

        override fun run() {
            try {
                val yesterdayLivenessReward = UserCall.yesterdayLivenessReward()
                when (UserCall.yesterdayLivenessReward()) {
                    null -> Log.error("获取昨日活跃度奖励失败")
                    -1 -> Log.info("已领取过昨日活跃度奖励")
                    else -> Log.info("获取昨日活跃度奖励成功，获得${yesterdayLivenessReward}积分")
                }
                currentRetryCount = 0
            } catch (e: Exception) {
                Log.error("获取昨日活跃度奖励失败, 稍后重试", e)
                if (currentRetryCount < retryCount) {
                    currentRetryCount++
                    timer.schedule(Task(retryCount - currentRetryCount), TimeUnit.MINUTES.toMillis(1L))
                }
            }

        }
    }

    override fun run() {
        timer.schedule(Task(5), TimeUnit.SECONDS.toMillis(5L), TimeUnit.DAYS.toMillis(1L))
    }

}