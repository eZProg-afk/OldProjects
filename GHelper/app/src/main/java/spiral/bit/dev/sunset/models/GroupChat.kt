package spiral.bit.dev.sunset.models

class GroupChat(
    var message: String, var sender: String, var timeStamp: String
) {
    constructor(): this(
        "",
        "",
        ""
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupChat

        if (message != other.message) return false
        if (sender != other.sender) return false
        if (timeStamp != other.timeStamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + sender.hashCode()
        result = 31 * result + timeStamp.hashCode()
        return result
    }


}