package ink.metoo.auto.fishpi.websocket

import com.google.gson.Gson
import com.google.gson.JsonObject
import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.ChatRoomCall
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatRoomWebSocketListener: WebSocketListener() {

    private val gson = Gson()

    override fun onMessage(webSocket: WebSocket, text: String) {
        val message = gson.fromJson(text, JsonObject::class.java)
        when (message.get("type").asString) {
            "msg" -> {
                val content = message.get("content").asString
                if (content.startsWith("{") && content.endsWith("}")) {
                    val contentJson = gson.fromJson(text, JsonObject::class.java)
                    when (contentJson.get("msgType").asString) {
                        "redPacket" -> doRedPacket(message.get("oid").asString, gson.fromJson(contentJson, RedPacket::class.java))
                     }
                }
            }
        }
    }

    private fun doRedPacket(oid: String, redPacket: RedPacket) {
        when (redPacket.type) {
            "random", "average" -> {
                ChatRoomCall.openRedPacket(oid)
                // TODO
                Log.info("领取了红包")
            }
            "specify"-> {

            }
            "heartbeat" -> {

            }
            "rockPaperScissors" -> {

            }
        }
    }

}