package spiral.bit.dev.lgbtswipe.views.msgs_views

interface MessageView {

    val id: String
    val from: String
    val timeStamp: String
    val fileUrl: String
    val text: String

    companion object {
        val MESSAGE_IMG: Int
            get() = 0
        val MESSAGE_TXT: Int
            get() = 1
        val MESSAGE_VOICE: Int
            get() = 2
        val MESSAGE_FILE: Int
            get() = 3
    }

    fun getTypeView(): Int
}