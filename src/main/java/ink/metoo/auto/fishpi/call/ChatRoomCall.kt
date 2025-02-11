package ink.metoo.auto.fishpi.call

import com.google.gson.Gson
import com.google.gson.JsonObject
import ink.metoo.auto.fishpi.ClientCaches
import ink.metoo.auto.fishpi.Requests
import ink.metoo.auto.fishpi.Settings
import okhttp3.WebSocketListener

object ChatRoomCall {

    class ChatRoomNode {
        var node: String? = null
        var name: String? = null
        var online: Int? = null
        var weight: Int? = null
    }

    class ChatRoomNodeResult : BaseResult() {
        var data: String? = null
        var avaliable: Array<ChatRoomNode>? = null
        var apiKey: String? = null
    }

    fun getChatRoomNode() = Requests.sendGetRequest<ChatRoomNodeResult>(
        path = "/chat-room/node/get",
        queryParam = arrayOf(Pair("apiKey", ClientCaches.apiKey))
    )

    fun watchChatRoom(listener: WebSocketListener) = Requests.watchWebSocket(
        url = ClientCaches.webSocketUrl,
        listener = listener
    )

    class SendMessageBody: BaseBody() {
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

    class OpenRedPacketBody : BaseBody() {
        var oId: String = ""
        var gesture: String? = null
    }

    fun openRedPacket(oId: String, gesture: String? = null) = Requests.sendJsonRequest<JsonObject> (
        path = "/chat-room/red-packet/open",
        body = OpenRedPacketBody().let {
            it.apiKey = ClientCaches.apiKey
            it.oId = oId
            it.gesture = gesture
            it
        }
    )

}