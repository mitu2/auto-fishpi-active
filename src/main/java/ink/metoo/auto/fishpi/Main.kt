package ink.metoo.auto.fishpi

import ink.metoo.auto.fishpi.call.ChatRoomCall
import ink.metoo.auto.fishpi.task.EverydayTask
import ink.metoo.auto.fishpi.task.LivenessTask
import ink.metoo.auto.fishpi.task.SendMessageTask

fun main() {
    Log.info("author by mitu2.")
    ChatRoomCall.sendMessage("![img](https://file.fishpi.cn/2025/02/01BE679FB0D1E665049A18B7BE7E5B5B-9bdc3c13.gif)<br />美好的一天从小B宅子开始")
    EverydayTask().run()
    LivenessTask().run()
    SendMessageTask().run()
}
