package ink.metoo.auto.fishpi.task

import ink.metoo.auto.fishpi.ClientCache
import ink.metoo.auto.fishpi.ClientCaches
import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.Settings
import ink.metoo.auto.fishpi.call.ChatRoomCall
import java.util.*
import java.util.concurrent.TimeUnit

class AutoSendMessageTask : Runnable {

    private val timer = Timer()
    private val random = Random()

    object Task : TimerTask() {

        private val haveSendMessage = mutableListOf<String>()
        private val messageQueue = Settings.autoTask.autoSendMessage.queues.sortedBy { it.type.ordinal }

        override fun run() {
            messageQueue.forEach {
                when (it.type) {
                    Settings.AutoTask.AutoSendMessage.MessageType.ONCE -> {
                        it.messages.forEach { message ->
                            when (it.condition) {
                                "DAY" -> {
                                    val alreadyMessage = ClientCaches.getAlreadyMessage().toMutableList()
                                    if (!alreadyMessage.contains(message)) {
                                        ChatRoomCall.sendMessage(message)
                                        Log.info("send type once message: $message")
                                        alreadyMessage.add(message)
                                        ClientCaches.setAlreadyMessage(alreadyMessage)
                                        return
                                    }
                                }
                                else -> if (!haveSendMessage.contains(message)) {
                                    ChatRoomCall.sendMessage(message)
                                    Log.info("send type once message: $message")
                                    haveSendMessage.add(message)
                                    return
                                }
                            }

                        }
                    }

                    Settings.AutoTask.AutoSendMessage.MessageType.WEEK -> {
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

                    Settings.AutoTask.AutoSendMessage.MessageType.REPEAT -> {
                        val message = it.messages.random()
                        ChatRoomCall.sendMessage(message)
                        Log.info("send type repeat message: $message")
                        return
                    }

                }
            }
        }
    }

    override fun run() {
        timer.schedule(
            Task,
            TimeUnit.SECONDS.toMillis(5),
            TimeUnit.MINUTES.toMillis(1) + random.nextLong(1L, TimeUnit.SECONDS.toMillis(10))
        )
    }


}
