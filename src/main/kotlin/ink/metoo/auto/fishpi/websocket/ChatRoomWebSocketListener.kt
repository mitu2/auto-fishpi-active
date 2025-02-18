package ink.metoo.auto.fishpi.websocket

import com.google.gson.Gson
import com.google.gson.JsonObject
import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.Settings
import ink.metoo.auto.fishpi.call.ChatRoomCall
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.apache.commons.lang3.math.NumberUtils
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

class ChatRoomWebSocketListener : WebSocketListener() {

    private val gson = Gson()
    private val timer = Timer()

    override fun onMessage(webSocket: WebSocket, text: String) {
        val message = gson.fromJson(text, Message::class.java)
        when (message.type) {
            "msg" -> {
                val content = message.content ?: return
                if (content.startsWith("{") && content.endsWith("}")) {
                    val contentJson = gson.fromJson(content, JsonObject::class.java)
                    Log.debug("websocket ${message.userName} json message: $text")
                    when (contentJson.get("msgType").asString) {
                        "redPacket" -> if (Settings.chatRoom.watchRedPacket) {
                            doRedPacket(message, gson.fromJson(contentJson, RedPacket::class.java))
                        }
                    }
                } else {
                    val md = message.md ?: return
                    if (md.startsWith("发红包") && message.userName == Settings.fishpiClient.username) {
                        val params = md.split(regex = "[ \n]".toPattern())
                        val name = params.getOrNull(1) ?: return
                        val money = NumberUtils.toInt(params.getOrNull(2), 32).coerceAtLeast(32)
                        val result = ChatRoomCall.sendRedPacket(
                            ChatRoomCall.RedPacketBody(
                                msg = params.getOrNull(3) ?: "给${name}的专属红包",
                                money = money,
                                count = 1,
                                recivers = arrayListOf(name),
                                type = "specify"
                            )
                        )
                        Log.info("向${name}发送了${money}积分红包, 发送结果: ${result.code}")
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
            "heartbeat", "random", "average" -> timer.schedule(timerTask {
                val result = ChatRoomCall.openRedPacket(message.oId!!)
                val me = result.who.findLast { it.userName == Settings.fishpiClient.username }
                if (me != null) {
                    Log.info("成功领取了${message.userName}的红包, 拿到了${me.userMoney}")
                } else {
                    Log.info("未领取到${message.userName}的红包, 是在下手慢了")
                }
            }, (TimeUnit.SECONDS.toMillis(1L)..TimeUnit.SECONDS.toMillis(3L)).random())

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
                        val year = (80..120).random() + (me?.userMoney ?: 0)
                        ChatRoomCall.sendMessage("蛇蛇老板${message.userName}专属的红包, 祝您活到${year}岁!")
                    }
                } catch (e: Exception) {
                    Log.error(e.message, e)
                }

            }

            "rockPaperScissors" -> {
//                println(Gson().toJson(ChatRoomCall.openRedPacket(message.oId!!, "0.1")))
            }
        }
    }

}