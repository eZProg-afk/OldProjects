package spiral.bit.dev.lgbtswipe.models

data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var birthDate: String = "",
    var gender: String = "",
    var photo: String = "",
    var status: String = "",
    var state: String = "",
    var textMsg: String = "",
    var typeMsg: String = "",
    var fromMsg: String = "",
    var timeStampMsg: Any = "",
    var fileUrlMsg: String = "empty",
    var userLastMessage: String = "",
    var phone: String = "",
    var about: String = "",
    var closedProfile: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        return (other as User).id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + birthDate.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + photo.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + textMsg.hashCode()
        result = 31 * result + typeMsg.hashCode()
        result = 31 * result + fromMsg.hashCode()
        result = 31 * result + timeStampMsg.hashCode()
        result = 31 * result + fileUrlMsg.hashCode()
        result = 31 * result + userLastMessage.hashCode()
        result = 31 * result + closedProfile.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + about.hashCode()
        return result
    }
}