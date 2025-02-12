package ink.metoo.auto.fishpi

import ink.metoo.auto.fishpi.task.AutoSendMessageTask
import ink.metoo.auto.fishpi.task.ChatRoomTask
import ink.metoo.auto.fishpi.task.LivenessTask
import ink.metoo.auto.fishpi.task.YesterdayLivenessRewardTask

object Main {
    fun isJar(): Boolean {
        return Main::class.java.getResource("")?.protocol == "jar"
    }
}

fun main() {
    Log.info("Happy every day. mitu2. mitu2.")
    if (Main.isJar()) {
        Log.info("检测到是jar环境启动请按照下面步骤填写")
        val console = System.console()
        Settings.fishpiClient.username = console.readLine("请输入鱼排账号: ")
        Settings.fishpiClient.password = console.readPassword("请输入鱼排密码: ").toString()
        Settings.fishpiClient.mfaCode = console.readPassword("请输入鱼排二级认证(没有回车): ").toString()
    }
    LivenessTask.run()
    ChatRoomTask.run()
    if (Settings.autoTask.autoSendMessage.enable) {
        AutoSendMessageTask.run()
    }
    if (Settings.autoTask.yesterdayLivenessReward.enable) {
        YesterdayLivenessRewardTask.run()
    }
}


