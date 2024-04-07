package spiral.bit.dev.lgbtswipe.models

data class Model(
    val id_moon: String = "",
    var nick_name_moon: String = "",
    var about_moon: String = "",
    var full_name_moon: String = "",
    var status_moon: String = "",
    var phone_moon: String = "",
    var photo_url_moon: String = "empty",

    var text_moon: String = "",
    var type_moon: String = "",
    var from_moon: String = "",
    var time_stamp_moon: Any = "",
    var file_url_moon: String = "empty",

    var last_message_moon: String = "",
    var choice_moon: Boolean = false,
    var is_msg_checked: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        return (other as Model).id_moon == id_moon
    }
}