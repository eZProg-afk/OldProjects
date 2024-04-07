package spiral.bit.dev.sunset.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_group_chat.*
import kotlinx.android.synthetic.main.toolbar_info.*
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.adapters.GroupChatAdapter
import spiral.bit.dev.sunset.models.Group
import spiral.bit.dev.sunset.models.GroupChat
import spiral.bit.dev.sunset.models.Member
import spiral.bit.dev.sunset.other.*

class GroupChatFragment(var group: Group) : Fragment() {

    private var groupChatsList = arrayListOf<GroupChat>()
    private lateinit var adapter: GroupChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_group_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadGroupInfo()

        checkIfWithoutRole()
        val ref = FirebaseDatabase.getInstance().reference.child("groups")
        ref.child(group.groupID).child("messages")
            .addValueEventListener(AppValueEventListener {
                for (ds: DataSnapshot in it.children) {
                    val model = ds.getValue(GroupChat::class.java)
                    if (model != null && !groupChatsList.contains(model)) groupChatsList.add(model)
                }
                adapter.submitList(groupChatsList)
                chat_recycler_view.smoothScrollToPosition(groupChatsList.size)
            })
        adapter = GroupChatAdapter(groupChatsList, view.context)
        chat_recycler_view.adapter = adapter
        chat_recycler_view.layoutManager = LinearLayoutManager(view.context)
        chat_btn_send_message.setOnClickListener {
            val msg = chat_input_msg.text.toString().trim()
            if (msg.isNotEmpty()) sendMsg(msg)
            else Toast.makeText(view.context, "Пожалуйста, введите сообщение!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun checkIfWithoutRole() {
        FirebaseDatabase.getInstance().reference.child("groups")
            .child(group.groupID).child("members")
            .addChildEventListener(AppChildEventListener {
                FirebaseDatabase.getInstance().reference.child("groups")
                    .child(group.groupID).child("members")
                    .child(it.key.toString())
                    .addListenerForSingleValueEvent(AppValueEventListener { snap ->
                        val member = snap.getValue(Member::class.java)
                        if (member != null && member.memberRole == "" && member.uid.equals(USER.userId)
                            && member.typeOfUser == "user") {
                            if (chat_input_msg != null) chat_input_msg.isEnabled = false
                            chat_input_msg.setOnClickListener {
                                Toast.makeText(
                                    view?.context,
                                    "Сейчас вам не выдана роль, и вы пока не можете ничего писать:(",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            if (chat_input_msg != null) chat_input_msg.isEnabled = true
                        }
                    })
            })
    }

    private fun sendMsg(message: String) {
        val timeStamp = System.currentTimeMillis().toString()
        val hashMap = HashMap<String, Any>()
        hashMap["sender"] = USER.userId.toString()
        hashMap["message"] = message
        hashMap["timeStamp"] = timeStamp
        val ref = FirebaseDatabase.getInstance().reference.child("groups")
        ref.child(group.groupID).child("messages").child(timeStamp).setValue(hashMap)
            .addOnSuccessListener {
                chat_input_msg.setText("")
                chat_recycler_view.smoothScrollToPosition(groupChatsList.size)
            }
    }

    private fun loadGroupInfo() {
        val ref = FirebaseDatabase.getInstance().reference.child("groups")
        ref.orderByChild("groupID").equalTo(group.groupID)
            .addValueEventListener(AppValueEventListener { snap ->
                for (ds: DataSnapshot in snap.children) {
                    val groupTitle = ds.child("groupTitle").value
                    if (toolbar_chat_fullname != null) toolbar_chat_fullname.text =
                        groupTitle.toString()
                    if (toolbar_chat_image != null) {
                        val path = FirebaseStorage.getInstance().reference.child("AVATARS_GROUPS")
                            .child(group.timeStamp)
                        getURL(path) { toolbar_chat_image.downloadAndSetImageToGroup(it) }
                    }
                    if (info_toolbar != null) info_toolbar.setOnClickListener {
                        changeFragments(
                            InsideGroupFragment(group),
                            true
                        )
                    }
                }
            })
    }
}