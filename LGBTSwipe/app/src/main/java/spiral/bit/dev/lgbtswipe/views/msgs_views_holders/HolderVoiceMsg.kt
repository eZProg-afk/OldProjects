package spiral.bit.dev.lgbtswipe.views.msgs_views_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.msgs_views.MessageView

class HolderVoiceMsg(view: View) : RecyclerView.ViewHolder(view),
    MessageHolder {

    private val mAppVoicePlayer = AppVoicePlayer()

    private val blockReceivedVoiceMessage: ConstraintLayout =
        view.findViewById(R.id.block_received_voice_message)
    private val blockUserVoiceMessage: ConstraintLayout =
        view.findViewById(R.id.block_user_voice_message)
    private val chatReceivedVoiceMessageTime: TextView =
        view.findViewById(R.id.chat_received_voice_message_time)
    private val chatUserVoiceMessageTime: TextView =
        view.findViewById(R.id.chat_user_voice_message_time)

    private val chatReceivedBtnPlay: ImageView = view.findViewById(R.id.chat_received_btn_play)
    private val chatUserBtnPlay: ImageView = view.findViewById(R.id.chat_user_btn_play)
    private val chatReceivedBtnStop: ImageView = view.findViewById(R.id.chat_received_btn_play)
    private val chatUserBtnStop: ImageView = view.findViewById(R.id.chat_user_btn_play)

    override fun drawMsg(view: MessageView) {
        if (view.from == CURRENT_USER_ID) {
            blockReceivedVoiceMessage.visibility = View.GONE
            blockUserVoiceMessage.visibility = View.VISIBLE
            chatUserVoiceMessageTime.text = view.timeStamp.asTime()
        } else {
            blockReceivedVoiceMessage.visibility = View.VISIBLE
            blockUserVoiceMessage.visibility = View.GONE
            chatReceivedVoiceMessageTime.text = view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
        mAppVoicePlayer.initPlayer()
        if (view.from == CURRENT_USER_ID) {
            chatUserBtnPlay.setOnClickListener {
                chatUserBtnPlay.visibility = View.GONE
                chatUserBtnStop.visibility = View.VISIBLE
                chatUserBtnStop.setOnClickListener {
                    stop {
                        chatUserBtnStop.setOnClickListener(null)
                        chatUserBtnPlay.visibility = View.VISIBLE
                        chatUserBtnStop.visibility = View.GONE
                    }
                }
                play(view) {
                    chatUserBtnPlay.visibility = View.VISIBLE
                    chatUserBtnStop.visibility = View.GONE
                }
            }
        } else {
            chatReceivedBtnPlay.setOnClickListener {
                chatReceivedBtnPlay.visibility = View.GONE
                chatReceivedBtnStop.visibility = View.VISIBLE
                chatReceivedBtnStop.setOnClickListener {
                    stop {
                        chatReceivedBtnStop.setOnClickListener(null)
                        chatReceivedBtnPlay.visibility = View.VISIBLE
                        chatReceivedBtnStop.visibility = View.GONE
                    }
                }
                play(view) {
                    chatReceivedBtnPlay.visibility = View.VISIBLE
                    chatReceivedBtnStop.visibility = View.GONE
                }
            }
        }
    }

    private fun play(view: MessageView, function: () -> Unit) =
        mAppVoicePlayer.playVoice(view.id, view.fileUrl) { function() }

    private fun stop(function: () -> Unit) = mAppVoicePlayer.stopPlayer { function() }

    override fun onDetach() {
        chatReceivedBtnPlay.setOnClickListener(null)
        chatUserBtnPlay.setOnClickListener(null)
        mAppVoicePlayer.releasePlayer()
    }
}