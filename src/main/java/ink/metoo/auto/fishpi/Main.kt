package ink.metoo.auto.fishpi

import ink.metoo.auto.fishpi.task.EverydayTask
import ink.metoo.auto.fishpi.task.LivenessTask
import ink.metoo.auto.fishpi.task.SendMessageTask

fun main() {
    Log.info("author by mitu2.")
    EverydayTask().run()
    LivenessTask().run()
    SendMessageTask().run()
}
