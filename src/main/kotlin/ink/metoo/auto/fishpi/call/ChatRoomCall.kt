package ink.metoo.auto.fishpi.call

import com.google.gson.Gson
import com.google.gson.JsonObject
import ink.metoo.auto.fishpi.ClientCaches
import ink.metoo.auto.fishpi.Settings
import ink.metoo.auto.fishpi.websocket.RedPacket
import okhttp3.WebSocketListener

object ChatRoomCall {

    class ChatRoomNode {
        var node: String? = null
        var name: String? = null
        var online: Int? = null
        var weight: Int? = null
    }

    class ChatRoomNodeResult : AbstractBaseResult() {
        var data: String? = null
        var avaliable: Array<ChatRoomNode>? = null
        var apiKey: String? = null
    }

    fun getChatRoomNode() = Requests.sendGetRequest<ChatRoomNodeResult>(
        path = "/chat-room/node/get",
        queryParams = arrayOf(Pair("apiKey", ClientCaches.apiKey))
    )

    fun watchChatRoom(listener: WebSocketListener) = Requests.watchWebSocket(
        url = getChatRoomNode().data ?: throw RuntimeException("get webSocketUrl fail"),
        listener = listener
    )

    class SendMessageBody : AbstractBaseBody() {
        var client: String? = null
        var content: Any? = null
    }

    fun sendMessage(content: Any) = Requests.sendJsonRequest<CodeResult>(
        path = "/chat-room/send",
        body = SendMessageBody().let {
            it.client = Settings.chatRoom.clientId
            it.apiKey = ClientCaches.apiKey
            it.content = content
            it
        }
    )


    data class RedPacketBody(
        val msg: String? = null,
        val money: Int = 32,
        val count: Int = 1,
        val recivers: List<String> = emptyList(),
        /**
         * 红包类型，random(拼手气红包), average(平分红包)，specify(专属红包)，heartbeat(心跳红包)，rockPaperScissors(猜拳红包)
         */
        val type: String,
        val gesture: String? = null
    )

    private val gson = Gson()

    fun sendRedPacket(body: RedPacketBody): CodeResult {
        val json = gson.toJson(body)
        return sendMessage("[redpacket]${json}[/redpacket]")
    }

    class OpenRedPacketBody : AbstractBaseBody() {
        var oId: String = ""
        var gesture: String? = null
    }

    class OpenRedPacket : AbstractBaseResult() {
        class Info {
            var count: Int? = null
            var gesture: Int? = null
            var got: String? = null
            var msg: String? = null
            var userName: String? = null
            // ...
        }

        var info: Info? = null
        var recivers: List<String> = emptyList()
        var who: List<RedPacket.User> = emptyList()
    }

    fun openRedPacket(oId: String, gesture: String? = null) = Requests.sendJsonRequest<OpenRedPacket>(
        path = "/chat-room/red-packet/open",
        body = OpenRedPacketBody().let {
            it.apiKey = ClientCaches.apiKey
            it.oId = oId
            it.gesture = gesture
            it
        }
    )

    fun getMessageRaw(oId: String) = Requests.sendGetRequest<String>(
        path = "/cr/raw/${oId}",
        queryParams = arrayOf(Pair("apiKey", ClientCaches.apiKey))
    )


    fun getContextMessage(
        oId: String = "",
        mode: Int = 0,
        size: Int = 1,
    ) = Requests.sendGetRequest<JsonObject>(
        path = "/chat-room/getMessage",
        queryParams = arrayOf(
            "apiKey" to ClientCaches.apiKey,
            "oId" to oId,
            "mode" to mode.toString(),
            "size" to size.toString()

        )
    )

}