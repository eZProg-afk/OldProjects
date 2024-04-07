package spiral.bit.dev.lgbtswipe.views.msgs_views_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.msgs_views.MessageView

class HolderTextMsg(view: View) : RecyclerView.ViewHolder(view),
    MessageHolder {

    private val blockReceivedMessage: ConstraintLayout =
        view.findViewById(R.id.block_received_message)
    private val blockUserMessage: ConstraintLayout =
        view.findViewById(R.id.block_user_message)
    private val chatUserMessage: TextView = view.findViewById(R.id.chat_user_message)
    private val chatReceivedMessage: TextView = view.findViewById(R.id.chat_received_message)
    private val chatReceivedMessageTime: TextView =
        view.findViewById(R.id.chat_received_message_time)
    private val chatUserMessageTime: TextView =
        view.findViewById(R.id.chat_user_message_time)

    override fun drawMsg(view: MessageView) {
        if (view.from == CURRENT_USER_ID) {
            blockUserMessage.visibility = View.VISIBLE
            blockReceivedMessage.visibility = View.GONE
            chatUserMessage.text = view.text
            chatUserMessageTime.text = view.timeStamp.asTime()
        } else {
            blockUserMessage.visibility = View.GONE
            blockReceivedMessage.visibility = View.VISIBLE
            chatReceivedMessage.text = view.text
            chatReceivedMessageTime.text = view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {}

    override fun onDetach() {}
}