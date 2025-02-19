package ink.metoo.auto.fishpi.websocket

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.ChatRoomCall
import okhttp3.WebSocket

object ChatRooms : Runnable {

    var ws: WebSocket? = null

    @Synchronized
    override fun run() {
        try {
            ws = ChatRoomCall.watchChatRoom(ChatRoomWebSocketListener())
        } catch (e: Exception) {
            Log.error("watch chatroom fail:" + e.message, e)
            ws?.cancel()
        }
    }

    @Synchronized
    fun cancel() {
        ws?.cancel()
        ws = null
    }

    @Synchronized
    fun refresh() {
        cancel()
        run()
    }

}
