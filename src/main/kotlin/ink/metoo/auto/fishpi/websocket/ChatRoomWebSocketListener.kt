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
import kotlin.concurrent.timerTask

class ChatRoomWebSocketListener : WebSocketListener() {

    private val gson = Gson()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.info("chatroom ${webSocket.request().url} open.")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        ChatRooms.cancel()
        Log.info("chatroom ${webSocket.request().url} onClosed. code: $code reason: $reason")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.info("chatroom ${webSocket.request().url} onClosing. code: $code reason: $reason")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val message = gson.fromJson(text, Message::class.java)
        val isMe = message.userName == Settings.fishpiClient.username
        try {
            when (message.type) {
                "msg" -> {
                    val content = message.content ?: return
                    if (content.startsWith("{") && content.endsWith("}")) {
                        val contentJson = gson.fromJson(content, JsonObject::class.java)
                        Log.debug("chatroom ${message.userName} json message: $text")
                        when (contentJson.get("msgType").asString) {
                            "redPacket" -> if (Settings.chatRoom.watchRedPacket) {
                                doRedPacket(message, gson.fromJson(contentJson, RedPacket::class.java), isMe)
                            }
                        }
                    } else CommandMessageHandler.handle(message, text)
                }
            }
        } catch (e: Throwable) {
            Log.error("chatroom message handler error ${e.message}", e)
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        if (t is java.io.EOFException) {
            ChatRooms.cancel()
        }
        Log.error("chatroom webSocket fail", t)
    }

    private fun doRedPacket(message: Message, redPacket: RedPacket, isMe: Boolean) {
        if (redPacket.count <= redPacket.got) {
            // 红包已领完 就不凑热闹了
            return
        }
        when (redPacket.type) {
            "heartbeat", "random", "average" -> ChatRooms.timer.schedule(timerTask {
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

            "rockPaperScissors" -> ChatRooms.timer.schedule(timerTask {
                if (!isMe && redPacket.money <= Settings.chatRoom.watchRockPaperScissorsMaxMoney) {
                    val result = ChatRoomCall.openRedPacket(message.oId!!, gesture = (0..2).random().toString())
                    val me = result.who.findLast { it.userName == Settings.fishpiClient.username }
                    if (me == null) {
                        Log.info("未领取到${message.userName}的猜拳红包, 是在下手慢了")
                        return@timerTask
                    }
                    val userMoney = me.userMoney ?: 0
                    if (userMoney < 0) {
                        Log.info("${message.userName}的猜拳红包, 是在下输了${userMoney}")
                    } else if (userMoney == 0) {
                        Log.info("${message.userName}的猜拳红包, 平局")
                    } else {
                        Log.info("${message.userName}的猜拳红包, 是在下赢了${userMoney}")
                    }
                }
            }, (TimeUnit.SECONDS.toMillis(1L)..TimeUnit.SECONDS.toMillis(3L)).random())
        }
    }

}