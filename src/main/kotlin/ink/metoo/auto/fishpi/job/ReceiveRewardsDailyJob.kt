package ink.metoo.auto.fishpi.job

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.UserCall
import org.quartz.Job
import org.quartz.JobExecutionContext

class ReceiveRewardsDailyJob : Job {

    override fun execute(context: JobExecutionContext) {
        try {
            when (val result = UserCall.yesterdayLivenessReward()) {
                null -> Log.error("获取昨日活跃度奖励失败")
                -1 -> Log.info("已领取过昨日活跃度奖励")
                else -> Log.info("获取昨日活跃度奖励成功，获得${result}积分")
            }
        } catch (e: Exception) {
            Log.error("获取昨日活跃度奖励异常", e)
        }

    }

}