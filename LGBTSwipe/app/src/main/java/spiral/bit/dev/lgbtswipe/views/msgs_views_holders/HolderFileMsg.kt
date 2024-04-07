package spiral.bit.dev.lgbtswipe.views.msgs_views_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.other.CURRENT_USER_ID
import spiral.bit.dev.lgbtswipe.other.asTime
import spiral.bit.dev.lgbtswipe.other.getFileFromStorage
import spiral.bit.dev.lgbtswipe.views.msgs_views.MessageView
import java.io.File

class HolderFileMsg(view: View) : RecyclerView.ViewHolder(view),
    MessageHolder {

    private val blockReceivedFileMessage: ConstraintLayout =
        view.findViewById(R.id.block_received_file_message)
    private val blockUserFileMessage: ConstraintLayout =
        view.findViewById(R.id.block_user_file_message)
    private val chatReceivedFileMessageTime: TextView =
        view.findViewById(R.id.chat_received_file_message_time)
    private val chatUserFileMessageTime: TextView =
        view.findViewById(R.id.chat_user_file_message_time)

    private val chatUserFilename: TextView = view.findViewById(R.id.chat_user_filename)
    private val chatUserBtnDownload: ImageView = view.findViewById(R.id.chat_user_btn_download)
    private val chatUserProgressBar: ProgressBar =
        view.findViewById(R.id.chat_user_progress_bar)

    private val chatReceivedFilename: TextView = view.findViewById(R.id.chat_received_filename)
    private val chatReceivedBtnDownload: ImageView =
        view.findViewById(R.id.chat_received_btn_download)
    private val chatReceivedProgressBar: ProgressBar =
        view.findViewById(R.id.chat_received_progress_bar)

    override fun drawMsg(view: MessageView) {
        if (view.from == CURRENT_USER_ID) {
            blockReceivedFileMessage.visibility = View.GONE
            blockUserFileMessage.visibility = View.VISIBLE
            chatUserFileMessageTime.text = view.timeStamp.asTime()
            chatUserFilename.text = view.text
        } else {
            blockReceivedFileMessage.visibility = View.VISIBLE
            blockUserFileMessage.visibility = View.GONE
            chatReceivedFileMessageTime.text = view.timeStamp.asTime()
            chatReceivedFilename.text = view.text
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == CURRENT_USER_ID) chatUserBtnDownload.setOnClickListener {
            clickToBtnFile(
                view
            )
        }
        else chatReceivedBtnDownload.setOnClickListener { clickToBtnFile(view) }
    }

    private fun clickToBtnFile(view: MessageView) {
        if (view.from == CURRENT_USER_ID) {
            chatUserBtnDownload.visibility = View.INVISIBLE
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            chatReceivedBtnDownload.visibility = View.INVISIBLE
            chatReceivedProgressBar.visibility = View.VISIBLE
        }

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )

        try {
            //if (hasPermissions()) TODO
            file.createNewFile()
            getFileFromStorage(file, view.fileUrl) {
                if (view.from == CURRENT_USER_ID) {
                    chatUserBtnDownload.visibility = View.VISIBLE
                    chatUserProgressBar.visibility = View.INVISIBLE
                } else {
                    chatReceivedBtnDownload.visibility = View.VISIBLE
                    chatReceivedProgressBar.visibility = View.INVISIBLE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        chatUserBtnDownload.setOnClickListener(null)
        chatReceivedBtnDownload.setOnClickListener(null)
    }


}