package ink.metoo.auto.fishpi.websocket

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.Settings
import ink.metoo.auto.fishpi.call.ChatRoomCall
import org.apache.commons.lang3.math.NumberUtils
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

object CommandMessageHandler {

    private val timer = Timer()
    private var isRockPaperScissorsIng = false

    fun handle(message: Message, text: String) {
        val md = message.md ?: return
        val isMe = message.userName == Settings.fishpiClient.username

        if (md.startsWith("发红包") && isMe) {
            val params = md.split(regex = "[ \n]".toPattern())
            val name = params.getOrNull(1) ?: return
            val money = NumberUtils.toInt(params.getOrNull(2), 32).coerceAtLeast(32)
            val result = ChatRoomCall.sendRedPacket(
                ChatRoomCall.RedPacketBody(
                    msg = params.getOrNull(3) ?: "给${name}的专属红包",
                    money = money,
                    count = 1,
                    recivers = arrayListOf(name),
                    type = "specify"
                )
            )
            Log.info("向${name}发送了${money}积分红包, 发送结果: ${result.code}")

        } else if (md.startsWith("发猜拳") && isMe) {
            if (isRockPaperScissorsIng) {
                return
            }
            isRockPaperScissorsIng = true
            val params = md.split(regex = "[ \n]".toPattern())
            var maxSize = NumberUtils.toInt(params.getOrNull(1), 1).coerceAtLeast(1)
            val money = NumberUtils.toInt(params.getOrNull(2), 32).coerceAtLeast(32)
            timer.schedule(timerTask {
                if (--maxSize < 0) {
                    isRockPaperScissorsIng = false
                    cancel()
                    return@timerTask
                }
                val result = ChatRoomCall.sendRedPacket(
                    ChatRoomCall.RedPacketBody(
                        msg = params.getOrNull(3) ?: "来此拳皇的猜拳",
                        money = money,
                        count = 1,
                        recivers = emptyList(),
                        type = "rockPaperScissors",
                        gesture = (0..2).random().toString()
                    )
                )
                Log.info("发送猜拳红包 ${result.code}")
            }, 0L, TimeUnit.SECONDS.toMillis(31L))
        }

    }

}