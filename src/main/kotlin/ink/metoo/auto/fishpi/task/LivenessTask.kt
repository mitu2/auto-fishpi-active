package ink.metoo.auto.fishpi.task

import ink.metoo.auto.fishpi.ClientCaches
import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.UserCall
import java.util.*
import java.util.concurrent.TimeUnit

object LivenessTask : Runnable {
    private val timer = Timer()

    override fun run() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                try {
                    ClientCaches.liveness = UserCall.getLiveness() ?: -1.0
                    Log.debug("liveness ${ClientCaches.liveness}%")
                } catch (e: Exception) {
                    Log.error("get liveness fail", e)
                }

            }
        }, TimeUnit.SECONDS.toMillis(15), TimeUnit.SECONDS.toMillis(31))
    }

}
