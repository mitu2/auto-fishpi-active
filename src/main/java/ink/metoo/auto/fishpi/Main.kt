package ink.metoo.auto.fishpi

import ink.metoo.auto.fishpi.call.ChatRoomCall
import ink.metoo.auto.fishpi.task.EverydayTask
import ink.metoo.auto.fishpi.task.LivenessTask
import ink.metoo.auto.fishpi.task.SendMessageTask
import ink.metoo.auto.fishpi.websocket.ChatRoomWebSocketListener

fun main() {
    Log.info("author by mitu2.")
//    ChatRoomCall.watchChatRoom(ChatRoomWebSocketListener())
    EverydayTask().run()
    LivenessTask().run()
    SendMessageTask().run()
}
