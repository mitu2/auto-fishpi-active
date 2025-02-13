package ink.metoo.auto.fishpi

import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.introspector.Property
import org.yaml.snakeyaml.introspector.PropertyUtils
import java.util.*
import java.util.stream.Collectors


object Settings {

    private val yaml = Yaml(Constructor(LoaderOptions()).let {
        it.propertyUtils = CustomPropertyUtils()
        it
    })

    class CustomPropertyUtils : PropertyUtils() {
        override fun getProperty(type: Class<*>?, name: String): Property {
            val camelCaseName = convertToCamelCase(name)
            return super.getProperty(type, camelCaseName)
        }

        private fun convertToCamelCase(name: String): String {
            val parts = name.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return Arrays.stream(parts)
                .map { part: String -> part.substring(0, 1).uppercase(Locale.getDefault()) + part.substring(1) }
                .collect(Collectors.joining(""))
                .replaceFirst("^.".toRegex(), name[0].toString())
        }
    }

    val setting: Setting by lazy {
        yaml.loadAs(javaClass.getResourceAsStream("/setting.yml"), Setting::class.java)
    }

    val fishpiClient: Client
        get() = setting.fishpiClient!!

    val chatRoom: ChatRoom
        get() = setting.chatRoom!!

    val jobs: List<Job>
        get() = setting.jobs!!

    class Setting {
        var fishpiClient: Client? = null
        var chatRoom: ChatRoom? = null
        var jobs: List<Job>? = null
    }

    class Job {
        var name: String = ""
        var target: String = ""
        var enable: Boolean = true
        var cron: String = ""
        var startExecute = false
    }

    class Client {
        var baseUrl: String = ""
        var userAgent: String = ""
        var username: String = ""
        var password: String = ""
        var mfaCode: String? = null
    }

    class ChatRoom {
        var clientId: String = ""
        var watchRedPacket: Boolean = true
        var messageQueue: List<MessageQueue> = emptyList()

        enum class MessageType {
            START_ONCE,
            DAY_ONCE,
            WEEK,
            REPEAT,
        }

        class MessageQueue {
            var type: MessageType = MessageType.DAY_ONCE
            var condition: String? = null
            var messages: List<String> = emptyList()
        }
    }

}
