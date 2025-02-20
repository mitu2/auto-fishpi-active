package ink.metoo.auto.fishpi.websocket

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.ChatRoomCall
import okhttp3.WebSocket
import java.util.*

object ChatRooms : Runnable {

    var ws: WebSocket? = null
    val timer = Timer()

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
        timer.cancel()
    }

    @Synchronized
    fun refresh() {
        cancel()
        run()
    }

}
