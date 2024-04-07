package spiral.bit.dev.lgbtswipe.views.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import android.widget.HorizontalScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.shashank.sony.fancytoastlib.FancyToast
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentSingleChatBinding
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.chat.SingleChatAdapter
import spiral.bit.dev.lgbtswipe.views.msgs_views.AppViewFactory

class SingleChatFragment(var user: User) : Fragment(R.layout.fragment_single_chat) {

    private val singleBinding: FragmentSingleChatBinding by viewBinding(FragmentSingleChatBinding::bind)
    private lateinit var listenerToolbar: AppValueEventListener
    private lateinit var receivingUser: User
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: ChildEventListener
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mSmoothScrollToPosition = true
    private var mIsScrolling = false
    private var mCountMessages = 10

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MAIN_ACTIVITY.hideMenu(false)
        initFields()
        initToolbar()
        initRecyclerView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initFields() {
        mBottomSheetBehavior =
            BottomSheetBehavior.from(singleBinding.bottomSheetChoice.bottomSheetChoice)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        mAppVoiceRecorder = AppVoiceRecorder()
        mSwipeRefreshLayout = singleBinding.chatSwipeRefresh
        mLayoutManager = LinearLayoutManager(this.context)
        singleBinding.chatInputMsg.addTextChangedListener(AppTextWatcher {
            val string = singleBinding.chatInputMsg.text.toString()
            if (string.isEmpty() || string == "Запись ГС...") {
                singleBinding.chatBtnSendMessage.visibility = View.GONE
                singleBinding.chatBtnAttach.visibility = View.VISIBLE
                singleBinding.chatBtnVoice.visibility = View.VISIBLE
            } else {
                singleBinding.chatBtnSendMessage.visibility = View.VISIBLE
                singleBinding.chatBtnAttach.visibility = View.GONE
                singleBinding.chatBtnVoice.visibility = View.GONE
            }
        })

        singleBinding.chatBtnAttach.setOnClickListener { attach() }

        CoroutineScope(Dispatchers.IO).launch {
            singleBinding.chatBtnVoice.setOnTouchListener { v, event ->
                if (hasPermissions(requireContext())) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        singleBinding.chatInputMsg.setText("Запись ГС...")
                        singleBinding.chatBtnVoice.setColorFilter(
                            ContextCompat.getColor(
                                ACTIVITY,
                                R.color.orange
                            )
                        )
                        val msgKey = getMsgKey(user.id)
                        mAppVoiceRecorder.startRecord(msgKey)
                    } else if (event.action == MotionEvent.ACTION_UP) {
                        singleBinding.chatInputMsg.setText("")
                        singleBinding.chatBtnVoice.colorFilter = null
                        mAppVoiceRecorder.stopRecord { file, messageKey ->
                            uploadFileInStorage(
                                Uri.fromFile(file),
                                messageKey,
                                user.id,
                                TYPE_MSG_VOICE
                            )
                            mSmoothScrollToPosition = true
                        }
                    }
                }
                true
            }
        }
    }

    private fun attach() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        singleBinding.bottomSheetChoice.btnAttachFile.setOnClickListener { attachFile() }
        singleBinding.bottomSheetChoice.btnAttachImage.setOnClickListener { attachImg() }
    }

    private fun attachFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    private fun attachImg() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .start(ACTIVITY, this)
    }

    private fun initRecyclerView() {
        mRecyclerView = singleBinding.chatRecyclerView
        mAdapter = SingleChatAdapter()
        mRefMessages = REFERENCE_DATABASE.child(NODE_MESSAGES)
            .child(CURRENT_USER_ID).child(user.id)
        mRecyclerView.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = mLayoutManager
        }

        mMessagesListener = AppChildEventListener { dataSnapshot ->

            val msg = dataSnapshot.getModel()
            if (mSmoothScrollToPosition) {
                mAdapter.addItemToBottom(AppViewFactory.getView(msg)) {
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            } else {
                mAdapter.addItemToTop(AppViewFactory.getView(msg)) {
                    mSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3) {
                    updateData()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }
        })

        mSwipeRefreshLayout.setOnRefreshListener { updateData() }
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
    }

    private fun initToolbar() {
        listenerToolbar = AppValueEventListener {
            receivingUser = it.getUser()!!
            initInfoToolbar()
        }

        mRefUser = REFERENCE_DATABASE.child(
            NODE_USERS
        ).child(user.id)
        mRefUser.addValueEventListener(listenerToolbar)
        singleBinding.chatBtnSendMessage.setOnClickListener {
            val msg = singleBinding.chatInputMsg.text.toString()
            if (msg.isEmpty()) {
                FancyToast.makeText(
                    context, "Введите сообщение!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.WARNING, false
                )
            } else sendMsg(msg, receivingUser.id, TYPE_TEXT) {
                singleBinding.chatInputMsg.setText("")
            }
        }
    }

    private fun changeIndicator() {
        if (receivingUser.state.equals("Онлайн", ignoreCase = true)) {
            singleBinding.infoToolbar.onlineIndicator.visibility = View.VISIBLE
        } else singleBinding.infoToolbar.onlineIndicator.visibility = View.GONE
    }

    private fun initInfoToolbar() {
        val path = user.id.let { REFERENCE_STORAGE.child(AVATARS_USERS).child(it) }
        getURL(path) { singleBinding.infoToolbar.toolbarChatImage.downloadAndSetImage(it) }
        singleBinding.infoToolbar.toolbarId.text = receivingUser.id
        singleBinding.infoToolbar.toolbarChatStatus.text = receivingUser.state
        singleBinding.infoToolbar.toolbarChatFullname.text = receivingUser.name
        singleBinding.infoToolbar.toolbarChatGender.text = receivingUser.gender
        changeIndicator()
    }

    //OVERRIDE

    override fun onPause() {
        super.onPause()
        mRefUser.removeEventListener(listenerToolbar)
        mRefMessages.removeEventListener(mMessagesListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAppVoiceRecorder.releaseRecorder()
        mAdapter.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val uri = CropImage.getActivityResult(data).uri
                    val msgKey = getMsgKey(user.id)
                    uploadFileInStorage(uri, msgKey, receivingUser.id, TYPE_MSG_IMG)
                    mSmoothScrollToPosition = true
                }
                PICK_FILE_REQUEST_CODE -> {
                    val uri = data.data
                    val msgKey = getMsgKey(user.id)
                    val filename = getFilenameFromUri(uri!!)
                    uploadFileInStorage(uri, msgKey, receivingUser.id, TYPE_MSG_FILE, filename)
                    mSmoothScrollToPosition = true
                }
            }
        }
    }
}