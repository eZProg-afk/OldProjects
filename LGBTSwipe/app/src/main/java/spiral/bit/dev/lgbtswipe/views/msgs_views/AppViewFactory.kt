package spiral.bit.dev.lgbtswipe.views.msgs_views

import spiral.bit.dev.lgbtswipe.models.Model
import spiral.bit.dev.lgbtswipe.other.TYPE_MSG_FILE
import spiral.bit.dev.lgbtswipe.other.TYPE_MSG_IMG
import spiral.bit.dev.lgbtswipe.other.TYPE_MSG_VOICE

class AppViewFactory {
    companion object {
        fun getView(message: Model): MessageView {
            return when (message.type_moon) {
                TYPE_MSG_IMG -> ViewImageMsg(
                    message.id_moon,
                    message.from_moon,
                    message.time_stamp_moon.toString(),
                    message.file_url_moon
                )
                TYPE_MSG_VOICE -> ViewVoiceMsg(
                    message.id_moon,
                    message.from_moon,
                    message.time_stamp_moon.toString(),
                    message.file_url_moon
                )
                TYPE_MSG_FILE -> ViewFileMsg(
                    message.id_moon,
                    message.from_moon,
                    message.time_stamp_moon.toString(),
                    message.file_url_moon,
                    message.text_moon
                )
                else -> ViewTextMsg(
                    message.id_moon,
                    message.from_moon,
                    message.time_stamp_moon.toString(),
                    message.file_url_moon,
                    message.text_moon
                )
            }
        }
    }
}