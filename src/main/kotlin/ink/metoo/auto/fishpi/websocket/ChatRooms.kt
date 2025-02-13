package ink.metoo.auto.fishpi.websocket

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.ChatRoomCall
import okhttp3.WebSocket

object ChatRooms : Runnable {

    var ws: WebSocket? = null

    override fun run() {
        try {
            ws = ChatRoomCall.watchChatRoom(ChatRoomWebSocketListener())
        } catch (e: Exception) {
            Log.error("watch chatroom fail:" + e.message, e)
            ws?.cancel()
        }
    }

    fun cancel() {
        ws?.cancel()
    }

    fun refresh() {
        cancel()
        run()
    }

}
