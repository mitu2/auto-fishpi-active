package ink.metoo.auto.fishpi

import ink.metoo.auto.fishpi.task.AutoSendMessageTask
import ink.metoo.auto.fishpi.task.ChatRoomTask
import ink.metoo.auto.fishpi.task.LivenessTask
import ink.metoo.auto.fishpi.task.YesterdayLivenessRewardTask

fun main() {
    Log.info("author by mitu2.")
    LivenessTask.run()
    ChatRoomTask.run()
    if (Settings.autoTask.autoSendMessage.enable) {
        AutoSendMessageTask.run()
    }
    if (Settings.autoTask.yesterdayLivenessReward.enable) {
        YesterdayLivenessRewardTask.run()
    }
}
