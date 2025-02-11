package ink.metoo.auto.fishpi.task

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.Settings
import ink.metoo.auto.fishpi.call.ChatRoomCall
import java.util.*
import java.util.concurrent.TimeUnit

class SendMessageTask : Runnable {

    private val timer = Timer()
    private val random = Random()
    private val haveSendMessage = mutableListOf<String>()
    private val messageQueue = Settings.chatRoom.queues.sortedBy { it.type.ordinal }

    override fun run() {
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    messageQueue.forEach {
                        when (it.type) {
                            Settings.ChatRoom.MessageType.ONCE -> {
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
                                    it.messages.forEach { message ->
                                        if (!haveSendMessage.contains(message)) {
                                            ChatRoomCall.sendMessage(message)
                                            Log.info("send type week message: $message")
                                            haveSendMessage.add(message)
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
            },
            TimeUnit.SECONDS.toMillis(5),
            TimeUnit.MINUTES.toMillis(1) + random.nextLong(1L, TimeUnit.SECONDS.toMillis(10))
        )
    }


}
