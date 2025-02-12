package ink.metoo.auto.fishpi.task

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.ChatRoomCall
import ink.metoo.auto.fishpi.websocket.ChatRoomWebSocketListener
import okhttp3.WebSocket

object ChatRoomTask : Runnable {

    private var ws: WebSocket? = null

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
