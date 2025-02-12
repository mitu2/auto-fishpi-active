package ink.metoo.auto.fishpi.task

import ink.metoo.auto.fishpi.ClientCaches
import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.UserCall
import java.util.*
import java.util.concurrent.TimeUnit

class LivenessTask : Runnable {
    private val timer = Timer()
    private val time = TimeUnit.SECONDS.toMillis(31)

    override fun run() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                try {
                    ClientCaches.liveness = UserCall.getLiveness() ?: -1.0
                    Log.debug("liveness ${ClientCaches.liveness}%")
                } catch (e: Exception) {
                    Log.debug("liveness ${ClientCaches.liveness}%")
                    Log.error("get liveness fail", e)
                }

            }
        }, time, time)
    }

}
