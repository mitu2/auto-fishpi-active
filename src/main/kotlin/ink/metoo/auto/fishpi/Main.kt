package ink.metoo.auto.fishpi

import ink.metoo.auto.fishpi.call.ChatRoomCall
import ink.metoo.auto.fishpi.task.YesterdayLivenessRewardTask
import ink.metoo.auto.fishpi.task.LivenessTask
import ink.metoo.auto.fishpi.task.AutoSendMessageTask
import ink.metoo.auto.fishpi.websocket.ChatRoomWebSocketListener

fun main() {
    Log.info("author by mitu2.")
    ChatRoomCall.watchChatRoom(ChatRoomWebSocketListener())
    LivenessTask().run()
    if (Settings.autoTask.autoSendMessage.enable) {
        AutoSendMessageTask().run()
    }
    if (Settings.autoTask.yesterdayLivenessReward.enable) {
        YesterdayLivenessRewardTask().run()
    }
}
