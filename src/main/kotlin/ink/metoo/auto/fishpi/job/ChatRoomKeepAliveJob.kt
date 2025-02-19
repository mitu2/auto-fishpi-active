package ink.metoo.auto.fishpi.job

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.websocket.ChatRooms
import org.quartz.Job
import org.quartz.JobExecutionContext

class ChatRoomKeepAliveJob : Job {

    override fun execute(context: JobExecutionContext) {
        val ws = ChatRooms.ws
        if (ws == null) {
            ChatRooms.refresh()
            return
        }
        ws.send("-hb-")
        Log.debug("chatroom websocket keep-alive")
    }

}