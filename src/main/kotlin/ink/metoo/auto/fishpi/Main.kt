package ink.metoo.auto.fishpi

import ink.metoo.auto.fishpi.job.AutoJobs
import ink.metoo.auto.fishpi.websocket.ChatRooms

object Main {
    fun isJar(): Boolean {
        return Main::class.java.getResource("")?.protocol == "jar"
    }
}

fun main() {
    Log.info("Happy every day. mitu2. mitu2.")
    if (Main.isJar()) {
        val console = System.console()
        Log.info("检测到是Jar环境启动请按照下面步骤填写")
        Settings.fishpiClient.username = console.readLine("请输入鱼排账号: ")
        Settings.fishpiClient.password = console.readLine("请输入鱼排密码: ")
        Settings.fishpiClient.mfaCode = console.readLine("请输入鱼排二级认证(没有回车): ")
    }
    AutoJobs.init()
    ChatRooms.run()
}


