package spiral.bit.dev.lgbtswipe.views.msgs_views_holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.views.msgs_views.MessageView

class AppHolderFactory {
    companion object {
        fun getHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                MessageView.MESSAGE_IMG -> {
                    val view = LayoutInflater.from(parent.context).inflate(
                        R.layout.message_item_image, parent, false
                    )
                    HolderImageMsg(view)
                }
                MessageView.MESSAGE_VOICE -> {
                    val view = LayoutInflater.from(parent.context).inflate(
                        R.layout.message_item_voice, parent, false
                    )
                    HolderVoiceMsg(view)
                }
                MessageView.MESSAGE_FILE -> {
                    val view = LayoutInflater.from(parent.context).inflate(
                        R.layout.message_item_file, parent, false
                    )
                    HolderFileMsg(view)
                }
                else -> {
                    val view = LayoutInflater.from(parent.context).inflate(
                        R.layout.message_item_text, parent, false
                    )
                    HolderTextMsg(view)
                }
            }
        }
    }
}