package spiral.bit.dev.lgbtswipe.views.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentChatsBinding
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.chat.ChatListAdapter

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private val chatsBinding: FragmentChatsBinding by viewBinding(FragmentChatsBinding::bind)

    private lateinit var chatAdapter: ChatListAdapter
    private val referenceChatList =
        REFERENCE_DATABASE.child(NODE_CHAT_LIST).child(CURRENT_USER_ID)
    private val referenceUsers = REFERENCE_DATABASE.child(NODE_USERS)
    private val referenceMessages =
        REFERENCE_DATABASE.child(NODE_MESSAGES).child(CURRENT_USER_ID)
    private var mListItems = arrayListOf<User>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.app_name)
        MAIN_ACTIVITY.hideMenu(true)
        hideKeyboard()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        chatAdapter = ChatListAdapter()
        referenceChatList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            dataSnapshot.children.forEach {
                referenceUsers.child(it.key.toString()).get().addOnSuccessListener { snap ->
                    val user = snap.getValue(User::class.java)
                    if (user != null) {
                        mListItems.add(user)
                        showChat(user)
                    }
                }
            }
        })
        chatsBinding.mainListRecycleView.adapter = chatAdapter
    }

    private fun showChat(user: User) {
        if (user.id == CURRENT_USER_ID) {
            referenceUsers.child(user.id)
                .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                    val newModel = dataSnapshot1.getUser()
                    referenceMessages.child(user.id).limitToLast(1)
                        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                            val tempList = dataSnapshot2.children.map { it.getUser() }
                            if (tempList.isEmpty()) {
                                newModel?.userLastMessage = "Начните общение!"
                            } else {
                                if (newModel?.userLastMessage?.length!! >= 15) {
                                    newModel.userLastMessage =
                                        "${tempList[0]?.textMsg.toString().substring(15)}..."
                                }
                            }
                            if (newModel?.name?.isEmpty() == true) newModel.name = newModel.photo
                            if (newModel != null) chatAdapter.updateListItems(newModel)
                        })
                })
        } else {
            referenceUsers.child(user.id)
                .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                    val newModel = dataSnapshot1.getUser()
                    referenceMessages.child(user.id).limitToLast(1)
                        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                            val tempList = dataSnapshot2.children.map { it.getUser() }
                            if (tempList.isEmpty()) {
                                newModel?.userLastMessage = "Начните общение!"
                            } else {
                                if (newModel?.userLastMessage?.length!! >= 15) {
                                    newModel.userLastMessage =
                                        "${tempList[0]?.textMsg.toString().substring(15)}..."
                                }
                            }

                            if (newModel?.name?.isEmpty() == true) newModel.name = newModel.email
                            if (newModel != null) chatAdapter.updateListItems(newModel)
                        })
                })
        }
    }
}