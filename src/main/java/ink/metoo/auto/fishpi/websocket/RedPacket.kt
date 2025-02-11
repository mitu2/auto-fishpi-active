package ink.metoo.auto.fishpi.websocket

class RedPacket {
    var msg: String? = null
    var recivers: List<String> = emptyList()
    var msgType: String? = null
    var count: Int? = null
    var type: String? = null
    var got: String? = null
    var who: List<User> = emptyList()

    class User {
        var userName: String? = null
        var avatar: String? = null
        var userMoney: Int? = null
        var time: String? = null

    }
}