package ink.metoo.auto.fishpi.websocket

class Message {

    /**
     * 消息类型online(在线) / discussChanged(话题变更) / revoke(撤回) / msg(聊天) / redPacketStatus(红包领取)
     */
    var type: String? = null

    /**
     * 	消息 Id
     */
    var oId: String? = null

    /**
     * 发布时间
     */
    var time: String? = null

    /**
     * 客户端来源
     */
    var client: String? = null

    /**
     * 消息内容
     */
    var content: String? = null
}