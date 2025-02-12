package ink.metoo.auto.fishpi.call

abstract class AbstractBaseResult {

    var code: Int? = null
    var msg: String? = null

}

class BaseResult : AbstractBaseResult()