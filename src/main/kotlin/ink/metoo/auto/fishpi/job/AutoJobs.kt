package ink.metoo.auto.fishpi.job

import ink.metoo.auto.fishpi.Log
import ink.metoo.auto.fishpi.Settings
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory


object AutoJobs {

    @Suppress("UNCHECKED_CAST")
    fun init() {
        val factory: SchedulerFactory = StdSchedulerFactory()
        val scheduler = factory.scheduler
        Settings.jobs.forEach {
            if (!it.enable) {
                return@forEach
            }
            val targetClass = Class.forName(it.target)
            if (Job::class.java.isAssignableFrom(targetClass)) {
                if (it.startExecute) {
                    val jobDetail = JobBuilder
                        .newJob(targetClass as Class<out Job>)
                        .withIdentity(it.name + "_now")
                        .build()
                    val startNowTrigger = TriggerBuilder.newTrigger()
                        .forJob(jobDetail)
                        .startNow()
                        .build()
                    scheduler.scheduleJob(jobDetail, startNowTrigger)
                }
                val jobDetail = JobBuilder
                    .newJob(targetClass as Class<out Job>)
                    .withIdentity(it.name)
                    .build()
                val triggerBuilder = TriggerBuilder.newTrigger()
                val trigger = triggerBuilder.forJob(jobDetail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(it.cron))
                    .build()
                scheduler.scheduleJob(jobDetail, trigger)
                Log.debug("load job ${it.name}. cron: ${it.cron}")
            }
            scheduler.start()
        }
    }

}