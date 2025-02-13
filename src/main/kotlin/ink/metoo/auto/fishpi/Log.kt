package ink.metoo.auto.fishpi

import java.text.SimpleDateFormat
import java.util.*

object Log {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    fun info(msg: Any?) {
        println("\uD83D\uDCDD ${dateFormat.format(Date())} [auto-fishpi-active] $msg")
    }

    fun debug(msg: Any?) {
        println("\uD83D\uDC1B ${dateFormat.format(Date())} [auto-fishpi-active] $msg")
    }

    fun error(msg: Any?, e: Throwable? = null) {
        println("❌ ${dateFormat.format(Date())} [auto-fishpi-active] $msg")
        e?.printStackTrace()
    }

}