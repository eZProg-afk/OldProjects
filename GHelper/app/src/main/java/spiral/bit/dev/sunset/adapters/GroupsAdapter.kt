package spiral.bit.dev.sunset.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.makeramen.roundedimageview.RoundedImageView
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.fragments.GroupChatFragment
import spiral.bit.dev.sunset.models.Group
import spiral.bit.dev.sunset.other.USER
import spiral.bit.dev.sunset.other.changeFragments
import spiral.bit.dev.sunset.other.downloadAndSetImageToGroup
import spiral.bit.dev.sunset.other.getURL

class GroupsAdapter(var listOfGroups: ArrayList<Group>) :
    RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder>() {

    private var dbUsersRef = FirebaseDatabase.getInstance().reference.child("users")
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private lateinit var context: Context

    inner class GroupsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupTitle: TextView = itemView.findViewById(R.id.item_group_title)
        val groupDescription: TextView = itemView.findViewById(R.id.item_group_description)
        val groupAvatar: RoundedImageView = itemView.findViewById(R.id.item_group_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        context = parent.context
        return GroupsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.group_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        val group = listOfGroups[position]
        holder.groupTitle.text = group.groupTitle
        holder.groupDescription.text = group.groupDescription
        holder.itemView.setOnClickListener {
            changeFragments(GroupChatFragment(group), true)
            saveToGroupList(group)
        }
        val path = storage.reference.child("AVATARS_GROUPS").child(group.timeStamp)
        getURL(path) { holder.groupAvatar.downloadAndSetImageToGroup(it) }
    }

    private fun saveToGroupList(group: Group) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            var tokenId = ""
            if (it.isComplete) tokenId = it.result.toString()
            Log.d("TAGCHECKTOKEN", "saveToGroupList:   $tokenId")
            dbUsersRef.child(USER.userId.toString()).child("user_groups").child(group.timeStamp).setValue(group)
            val hashMap = HashMap<String, String>()
            hashMap["uid"] = USER.userId.toString()
            hashMap["memberNickName"] = USER.userNickName
            hashMap["memberEmail"] = USER.userEmail
            hashMap["memberPassword"] = USER.userPassword
            hashMap["memberRole"] = ""
            hashMap["typeOfUser"] = "user"
            hashMap["memberNote"] = ""
            hashMap["tokenId"] = tokenId
            val ref = FirebaseDatabase.getInstance().reference.child("groups")
            ref.child(group.timeStamp).child("members").child(USER.userId.toString())
                .setValue(hashMap).addOnSuccessListener {
                    Toast.makeText(context, "Вы вступили!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount(): Int = listOfGroups.size

    fun submitGroups(listGroups: ArrayList<Group>) {
        listOfGroups = listGroups
        notifyDataSetChanged()
    }
}