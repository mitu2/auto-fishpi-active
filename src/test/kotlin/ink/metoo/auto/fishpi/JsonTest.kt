package ink.metoo.auto.fishpi

import com.google.gson.Gson
import org.junit.Test

class JsonTest {
    @Test
    fun testCase() {
        val json = "\"[\\\"sevenSummer\\\"]\""
        val gson = Gson()
        val substring = json.substring(1, json.length - 1).replace("\\", "")
        println(substring)
        println(gson.fromJson(substring, Array<String>::class.java))
    }
}
