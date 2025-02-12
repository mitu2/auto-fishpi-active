package ink.metoo.auto.fishpi.websocket

import com.google.gson.Gson
import com.google.gson.JsonObject
import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.Settings
import ink.metoo.auto.fishpi.call.ChatRoomCall
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatRoomWebSocketListener : WebSocketListener() {

    private val gson = Gson()

    override fun onMessage(webSocket: WebSocket, text: String) {
        val message = gson.fromJson(text, JsonObject::class.java)
        when (message.get("type")?.asString) {
            "msg" -> {
                val content = message.get("content")?.asString
                if (content != null && content.startsWith("{") && content.endsWith("}")) {
                    val contentJson = gson.fromJson(content, JsonObject::class.java)
                    Log.debug("websocket special message: $content")
                    when (contentJson.get("msgType").asString) {
                        "redPacket" -> doRedPacket(
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
        when (redPacket.type) {
            "random", "average" -> {
                val result = ChatRoomCall.openRedPacket(message.oId!!)
                val me = result.who.findLast { it.userName == Settings.fishpiClient.username }
                if (me != null) {
                    Log.info("成功领取了${result.info?.userName}的红包, 拿到了${me.userMoney}")
                } else {
                    Log.info("未领取到${result.info?.userName}的红包, 是在下手慢了")
                }
            }

            "specify" -> {
                try {
                    val recivers = gson.toJson(redPacket.recivers, Array<String>::class.java)
                    if (recivers.contains(Settings.fishpiClient.username)) {
                        val result = ChatRoomCall.openRedPacket(message.oId!!)
                        val me = result.who.findLast { it.userName == Settings.fishpiClient.username }
                        Log.info("成功领取了${result.info?.userName}的专属红包, 拿到了${me?.userMoney}")
                    }
                } catch (e: Exception) {
                    Log.error(e.message, e)
                }

            }

            "heartbeat" -> {

            }

            "rockPaperScissors" -> {

            }
        }
    }

}