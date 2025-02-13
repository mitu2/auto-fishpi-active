package ink.metoo.auto.fishpi.job

import ink.metoo.auto.fishpi.ClientCaches
import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.call.UserCall
import org.quartz.Job
import org.quartz.JobExecutionContext

class WatchLivenessJob: Job {

    override fun execute(context: JobExecutionContext?) {
        try {
            val newestLiveness = UserCall.getLiveness() ?: -1.0
            if (newestLiveness != ClientCaches.liveness) {
                ClientCaches.liveness = newestLiveness
                Log.debug("liveness ${ClientCaches.liveness}%")
            }
        } catch (e: Exception) {
            Log.error("get liveness fail", e)
        }
    }

}