package ink.metoo.auto.fishpi.job

import ink.metoo.auto.fishpi.ClientCaches
import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.Settings
import ink.metoo.auto.fishpi.call.ChatRoomCall
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.util.*

class AutoSendMessageJob : Job {

    private val haveSendMessage = mutableListOf<String>()
    private val messageQueue = Settings.chatRoom.messageQueue.sortedBy { it.type.ordinal }

    override fun execute(context: JobExecutionContext?) {
        // 如果已经水满，或者活跃度未获得前，则不发送消息
        if (ClientCaches.liveness < 0.0 || ClientCaches.liveness >= 100.0) {
            return
        }
        messageQueue.forEach {
            when (it.type) {
                Settings.ChatRoom.MessageType.DAY_ONCE -> {
                    it.messages.forEach { message ->
                        val alreadyMessage = ClientCaches.getAlreadyMessage().toMutableList()
                        if (!alreadyMessage.contains(message)) {
                            ChatRoomCall.sendMessage(message)
                            Log.info("send type once message: $message")
                            alreadyMessage.add(message)
                            ClientCaches.setAlreadyMessage(alreadyMessage)
                            return
                        }
                    }
                }

                Settings.ChatRoom.MessageType.START_ONCE -> {
                    it.messages.forEach { message ->
                        if (!haveSendMessage.contains(message)) {
                            ChatRoomCall.sendMessage(message)
                            Log.info("send type once message: $message")
                            haveSendMessage.add(message)
                            return
                        }
                    }

                }

                Settings.ChatRoom.MessageType.WEEK -> {
                    val day = it.condition?.run {
                        when (val week = toInt()) {
                            7 -> 1
                            else -> week + 1
                        }
                    }
                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == day) {
                        val alreadyMessage = ClientCaches.getAlreadyMessage().toMutableList()
                        it.messages.forEach { message ->
                            if (!alreadyMessage.contains(message)) {
                                ChatRoomCall.sendMessage(message)
                                Log.info("send type week message: $message")
                                alreadyMessage.add(message)
                                ClientCaches.setAlreadyMessage(alreadyMessage)
                                return
                            }
                        }
                    }
                }

                Settings.ChatRoom.MessageType.REPEAT -> {
                    val message = it.messages.random()
                    ChatRoomCall.sendMessage(message)
                    Log.info("send type repeat message: $message")
                    return
                }

            }
        }
    }
}