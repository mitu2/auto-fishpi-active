package ink.metoo.auto.fishpi

import com.google.gson.Gson
import ink.metoo.auto.fishpi.call.ChatRoomCall
import ink.metoo.auto.fishpi.call.UserCall
import ink.metoo.auto.fishpi.websocket.ChatRooms
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.Path

object ClientCaches {

    private val userDir = System.getProperty("user.home")!!
    private val workDir = "${userDir}/.auto-fishpi-active"
    private var cache = ClientCache(workDir)

    var liveness: Double = -1.0

    val apiKey: String
        get() = cache.apiKey

    val webSocketUrl: String
        get() = cache.webSocketUrl

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val gson = Gson()


    fun getAlreadyMessage(): List<String> {
        val file = Path("${workDir}/log/${Settings.fishpiClient.username}/${dateFormat.format(Date())}.json").toFile()
        if (file.exists()) {
            try {
                return gson.fromJson(file.reader(), Array<String>::class.java).toList()
            } catch (e: Exception) {
                Log.error(e.message, e)
                return emptyList()
            }
        } else {
            file.parentFile.let {
                it.exists() || it.mkdirs()
            }
            return emptyList()
        }
    }

    fun setAlreadyMessage(arr: List<String>) {
        val file = Path("${workDir}/log/${Settings.fishpiClient.username}/${dateFormat.format(Date())}.json").toFile()
        file.parentFile.let {
            it.exists() || it.mkdirs()
        }
        file.exists() || file.createNewFile()
        file.writeText(gson.toJson(arr))
    }

    fun refresh() {
        val dir = Path("${workDir}/cache/${Settings.fishpiClient.username}.key").toFile()
        if (dir.exists()) {
            dir.delete()
        }
        cache = ClientCache(workDir)
        liveness = -1.0
        Log.info("refresh apiKey")
        ChatRooms.refresh()
    }

}

class ClientCache(workDir: String) {

    private val keyFile = Path("${workDir}/cache/${Settings.fishpiClient.username}.key").toFile()

    val apiKey: String by lazy {
        Log.info("login user: ${Settings.setting.fishpiClient?.username}")
        if (keyFile.exists()) {
            val readText = keyFile.readText()
            Log.info("read cache key $keyFile {$readText}")
            return@lazy readText
        }
        keyFile.parentFile.mkdirs()
        UserCall.getKey().run {
            val key = key ?: throw RuntimeException(msg?.let { "get api-key fail cause: $it" } ?: "get key fail")
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