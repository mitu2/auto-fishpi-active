package ink.metoo.auto.fishpi

import ink.metoo.auto.fishpi.call.ChatRoomCall
import ink.metoo.auto.fishpi.call.UserCall
import okhttp3.OkHttpClient
import kotlin.io.path.Path

object ClientCaches {

    private val userDir = System.getProperty("user.home")!!
    private val workDir = "${userDir}/.auto-fishpi-active"
    private var cache = ClientCache(workDir)

    var liveness: Double = -1.0

    val okhttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    val apiKey: String
        get() = cache.apiKey

    val webSocketUrl: String
        get() = cache.webSocketUrl

    fun refresh() {
        val dir = Path("${workDir}/cache").toFile()
        if (dir.exists()) {
            dir.deleteRecursively()
        }
        cache = ClientCache(workDir)
    }

}

class ClientCache(workDir: String) {

    private val keyFile = Path("${workDir}/cache/key").toFile()

    val apiKey: String by lazy {
        Log.info("login user: ${Settings.setting.fishpiClient?.username}")
        if (keyFile.exists()) {
            val readText = keyFile.readText()
            Log.info("read cache key $keyFile {$readText}")
            return@lazy readText
        }
        keyFile.parentFile.mkdirs()
        UserCall.getKey().run {
            val key = key ?: throw RuntimeException(msg?.let { "get key fail cause: $it" } ?: "get key fail")
            Log.info("request key {$key}")
            keyFile.writeText(key)
            Log.info("save cache key to $keyFile")
            key
        }
    }

    val webSocketUrl: String by lazy {
        ChatRoomCall.getChatRoomNode().data ?: throw RuntimeException("get webSocketUrl fail")
    }


}