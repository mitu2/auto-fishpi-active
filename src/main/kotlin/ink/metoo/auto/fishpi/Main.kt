package ink.metoo.auto.fishpi

import ink.metoo.auto.fishpi.job.AutoJobs

class Main(private val args: Array<String>) {

    val hasInitCommand: Boolean
        get() = args.contains("-init")

    val hasConsoleCommand: Boolean
        get() = args.contains("-console")

    val username: String
        get() = getArgValue("username") ?: Settings.fishpiClient.username

    val password: String
        get() = getArgValue("password") ?: Settings.fishpiClient.password

    val mfaCode: String?
        get() = getArgValue("mfaCode")

    private fun getArgValue(key: String): String? {
        val kn = "-$key:"
        return args.findLast { it.startsWith(kn) }?.run {
            split(kn).getOrNull(1)
        }
    }
}

fun isJar(): Boolean {
    return Main::class.java.getResource("")?.protocol == "jar"
}

fun main(args: Array<String>) {
    Log.info("Happy every day. mitu2. mitu2.")
    val main = Main(args)
    if (isJar()) {
        if (main.hasConsoleCommand || (!main.hasInitCommand && !Settings.localSettingExists)) {
            val console = System.console()
            Log.info("检测到是Jar环境启动请按照下面步骤填写")
            Settings.fishpiClient.username = console.readLine("请输入鱼排账号: ")
            Settings.fishpiClient.password = console.readLine("请输入鱼排密码: ")
            Settings.fishpiClient.mfaCode = console.readLine("请输入鱼排二级认证(没有回车): ")
        } else {
            if (main.hasInitCommand) {
                Settings.initSettingFile()
                return
            }
            Settings.fishpiClient.username = main.username
            Settings.fishpiClient.password = main.password
            Settings.fishpiClient.mfaCode = main.mfaCode
        }
    } else {
        Settings.fishpiClient.username = main.username
        Settings.fishpiClient.password = main.password
        Settings.fishpiClient.mfaCode = main.mfaCode
    }
    AutoJobs.init()
}


