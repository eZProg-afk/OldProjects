package spiral.bit.dev.lgbtswipe.views.main.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.SingleChatFragment
import java.net.URL

class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.ChatListHolder>() {

    private var listItems = mutableListOf<User>()

    class ChatListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.main_list_item_name)
        val itemLastMessage: TextView = view.findViewById(R.id.main_list_last_message)
        val itemPhoto: RoundedImageView = view.findViewById(R.id.main_list_item_photo)
        val callImageView: ImageView = view.findViewById(R.id.call_image)
        val mainItem: ConstraintLayout = view.findViewById(R.id.main_item)
        //val onlineImg: ImageView = view.findViewById(R.id.online_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        return ChatListHolder(view)
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: ChatListHolder, position: Int) {
        holder.itemName.text = listItems[position].name
        holder.itemLastMessage.text = listItems[position].userLastMessage
        val path = REFERENCE_STORAGE.child(AVATARS_USERS).child(listItems[position].id)
        getURL(path) { holder.itemPhoto.downloadAndSetImage(it) }
//        if (holder.onlineImg.status_moon.equals(UserStatus.ONLINE)) {
//
//        }
        holder.callImageView.setOnClickListener {

            val serverUrl: URL = URL("https://meet.jit.si")
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(holder.itemLastMessage.text as String + holder.itemName.text)
                .setServerURL(serverUrl)
                .setWelcomePageEnabled(false)
                .build()

            JitsiMeetActivity.launch(it.context, options)
        }
        holder.mainItem.setOnClickListener { changeMainFragments(SingleChatFragment(listItems[position])) }
    }

    fun updateListItems(item: User) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}