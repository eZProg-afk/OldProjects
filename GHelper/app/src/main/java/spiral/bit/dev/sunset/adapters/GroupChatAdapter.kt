package spiral.bit.dev.sunset.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.models.GroupChat
import spiral.bit.dev.sunset.other.AppValueEventListener
import spiral.bit.dev.sunset.other.MSG_TYPE_RECEIVED
import spiral.bit.dev.sunset.other.MSG_TYPE_USER
import spiral.bit.dev.sunset.other.USER
import java.util.*
import kotlin.collections.ArrayList

class GroupChatAdapter(var modelGroupList: ArrayList<GroupChat>, var context: Context):
    RecyclerView.Adapter<GroupChatAdapter.GroupChatHolder>() {

    inner class GroupChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.chat_user_name)
        var tvMessage: TextView = itemView.findViewById(R.id.chat_user_message)
        var tvTime: TextView = itemView.findViewById(R.id.chat_user_message_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChatHolder {
        return if (viewType == MSG_TYPE_USER) {
            val view = LayoutInflater.from(context).inflate(R.layout.message_item_me, parent, false)
            GroupChatHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.message_item_other, parent, false)
            GroupChatHolder(view)
        }
    }

    override fun onBindViewHolder(holder: GroupChatHolder, position: Int) {
        val model = modelGroupList[position]
        val msg = model.message
        val time = model.timeStamp
        val senderID = model.sender
        holder.tvMessage.text = msg
        val cal = Calendar.getInstance(Locale.getDefault())
        cal.timeInMillis = time.toLong()
        val dateTimeFormat = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString()
        holder.tvTime.text = dateTimeFormat
        setUserName(model, holder)
    }

    private fun setUserName(model: GroupChat, holder: GroupChatHolder) {
        val ref = FirebaseDatabase.getInstance().reference.child("users")
        ref.orderByChild("userId").equalTo(model.sender)
            .addValueEventListener(AppValueEventListener {
                for (ds: DataSnapshot in it.children) {
                    val nickName = ds.child("userNickName").value
                    holder.tvName.text = nickName.toString()
                }
            })
    }

    override fun getItemViewType(position: Int): Int {
        return if (modelGroupList[position].sender == USER.userId.toString()) {
            MSG_TYPE_USER
        } else MSG_TYPE_RECEIVED
    }

    override fun getItemCount(): Int = modelGroupList.size

    fun submitList(groupChatArrayList: ArrayList<GroupChat>) {
        modelGroupList = groupChatArrayList
        notifyDataSetChanged()
    }
}