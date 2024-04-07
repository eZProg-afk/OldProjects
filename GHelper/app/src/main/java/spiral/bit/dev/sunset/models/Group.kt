package spiral.bit.dev.sunset.models

class Group(var groupID: String, var groupTitle: String, var groupDescription: String,
var timeStamp: String) {
    constructor(): this("","", "", "")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        if (groupID != other.groupID) return false
        if (groupTitle != other.groupTitle) return false
        if (groupDescription != other.groupDescription) return false
        if (timeStamp != other.timeStamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = groupID.hashCode()
        result = 31 * result + groupTitle.hashCode()
        result = 31 * result + groupDescription.hashCode()
        result = 31 * result + timeStamp.hashCode()
        return result
    }

}