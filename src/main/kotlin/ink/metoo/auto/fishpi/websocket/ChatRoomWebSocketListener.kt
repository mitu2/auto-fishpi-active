package ink.metoo.auto.fishpi.websocket

import com.google.gson.Gson
import com.google.gson.JsonObject
import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.Settings
import ink.metoo.auto.fishpi.call.ChatRoomCall
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.*
import java.util.concurrent.TimeUnit

class ChatRoomWebSocketListener : WebSocketListener() {

    private val gson = Gson()
    private val random = Random()
    private val timer = Timer()

    override fun onMessage(webSocket: WebSocket, text: String) {
        val message = gson.fromJson(text, JsonObject::class.java)
        when (message.get("type")?.asString) {
            "msg" -> {
                val content = message.get("content")?.asString
                if (content != null && content.startsWith("{") && content.endsWith("}")) {
                    val contentJson = gson.fromJson(content, JsonObject::class.java)
                    Log.debug("websocket special message: $content")
                    when (contentJson.get("msgType").asString) {
                        "redPacket" -> if (Settings.chatRoom.watchRedPacket) doRedPacket(
                            gson.fromJson(message, Message::class.java),
                            gson.fromJson(contentJson, RedPacket::class.java)
                        )
                    }
                }
            }
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.error("websocket fail", t)
    }

    private fun doRedPacket(message: Message, redPacket: RedPacket) {
        if (redPacket.count <= redPacket.got) {
            // 红包已领完 就不凑热闹了
            return
        }
        when (redPacket.type) {
            "heartbeat", "random", "average" -> timer.schedule(object : TimerTask() {
                override fun run() {
                    val result = ChatRoomCall.openRedPacket(message.oId!!)
                    val me = result.who.findLast { it.userName == Settings.fishpiClient.username }
                    val userName = result.info?.userName
                    if (me != null) {
                        Log.info("成功领取了${userName}的红包, 拿到了${me.userMoney}")
                    } else {
                        Log.info("未领取到${userName}的红包, 是在下手慢了")
                    }
                }
            }, (0L..TimeUnit.SECONDS.toMillis(3L)).random())

            "specify" -> {
                try {
                    val reciversStr = redPacket.recivers
                    val recivers = gson.fromJson(
                        reciversStr.replace("\\", ""),
                        Array<String>::class.java
                    )
                    if (recivers.contains(Settings.fishpiClient.username)) {
                        val result = ChatRoomCall.openRedPacket(message.oId!!)
                        val me = result.who.findLast { it.userName == Settings.fishpiClient.username }
                        Log.info("成功领取了${result.info?.userName}的专属红包, 拿到了${me?.userMoney}")
                        val userName = result.info?.userName
                        val year = (80..120).random() + (me?.userMoney ?: 0)
                        ChatRoomCall.sendMessage("蛇蛇老板${userName}专属的红包, 祝您活到${year}岁!")
                    }
                } catch (e: Exception) {
                    Log.error(e.message, e)
                }

            }

            "rockPaperScissors" -> {

            }
        }
    }

}