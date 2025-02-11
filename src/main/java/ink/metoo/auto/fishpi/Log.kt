package ink.metoo.auto.fishpi

object Log {

    fun info(msg: Any) {
        println("\uD83D\uDCDD [auto-fishpi-active] $msg")
    }

    fun debug(msg: Any) {
        println("\uD83D\uDC1B [auto-fishpi-active] $msg")
    }

    fun error(msg: Any) {
        println("❌ [auto-fishpi-active] $msg")
    }

}