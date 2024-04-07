package spiral.bit.dev.lgbtswipe.views.main

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentCardsBinding
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.chat.CardStackAdapter
import javax.inject.Inject

//ХОТЕЛОСЬ БЫ СЮДА В РАЗМЕТКУ СВИТЧ ЗАПИЗДЯЧИТ ТИПА ПОИСК ПО ГЕНДЕРУ И ПРИ ТЫКЕ НА СВИТЧ ОН ПОИСК ТОК ПО ГЕНДЕРУ А ПРИ СВИТЧ ОФФ
//ПО ВСЕЙ БАЗЕ, TODO

@AndroidEntryPoint
class CardsFragment : Fragment(R.layout.fragment_cards), CardStackListener {

    private val cardsBinding: FragmentCardsBinding by viewBinding(FragmentCardsBinding::bind)

    @Inject
    lateinit var db: FirebaseDatabase

    @Inject
    lateinit var storageInstance: FirebaseStorage

    @Inject
    lateinit var authClient: FirebaseAuth

    @Inject
    lateinit var mainPrefs: SharedPreferences

    private lateinit var rowItems: ArrayList<User>
    private lateinit var dbRef: DatabaseReference
    private lateinit var manager: CardStackLayoutManager
    private lateinit var cardAdapter: CardStackAdapter


    override fun onResume() {
        super.onResume()
          if (mainPrefs.contains("isOnlyByUserGender")) {
            getUsersByMyGender()
        } else findAllUsers()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MAIN_ACTIVITY.hideMenu(true)
        initVars()
        initCardStack()
        initListeners()
    }

    private fun initVars() {
        REFERENCE_STORAGE = storageInstance.reference
        CURRENT_USER_ID = authClient.uid.toString()
        dbRef = db.reference
    }

    private fun initListeners() {
        cardsBinding.reswipeBtn.setOnClickListener { cardsBinding.cardStackView.rewind() }

        cardsBinding.likeBtn.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            val likedUser = rowItems[manager.topPosition]

            val pathToSymp = REFERENCE_DATABASE.child(NODE_USERS)
                .child(likedUser.id).child(NODE_SYMPATHIES)
            pathToSymp.child(likedUser.id)
                .child(CHILD_ID).setValue(likedUser.id)

            cardsBinding.cardStackView.swipe()
        }

        cardsBinding.cancelBtn.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardsBinding.cardStackView.swipe()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initCardStack() {
        rowItems = if (mainPrefs.contains("isOnlyByUserGender")) {
            arrayListOf()
        } else findAllUsers()
        manager = CardStackLayoutManager(context, this)
        cardsBinding.cardStackView.layoutManager = manager
        cardAdapter = CardStackAdapter(rowItems, requireContext())
        cardsBinding.cardStackView.adapter = cardAdapter
        manager.setOverlayInterpolator(LinearInterpolator())
        manager.setStackFrom(StackFrom.Right)
        manager.setMaxDegree(15F)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setVisibleCount(3)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeThreshold(0.3F)
    }

    private fun findAllUsers(): ArrayList<User> {
        val items: ArrayList<User> = ArrayList()
        dbRef.child(NODE_USERS)
            .addChildEventListener(AppChildEventListener {
                if (it.exists()) {
                    val user = it.key?.let { it1 ->
                        User(
                            it1, it.child("name").value.toString(),
                            it.child("email").value.toString(),
                            it.child("password").value.toString(),
                            it.child("birthDate").value.toString(),
                            it.child("gender").value.toString(),
                            it.child("photo").value.toString()
                        )
                    }
                    if (user != null) items.add(user)
                    cardAdapter.notifyDataSetChanged()
                }
            })
        return items
    }

    private fun getUsersByMyGender() {
        dbRef.child(NODE_USERS).addChildEventListener(AppChildEventListener {
            it.children.forEach { childSnap ->
                if (childSnap.value?.toString().equals(USER.gender)) {
                    if (it.exists()) {
                        val user = it.key?.let { it1 ->
                            User(
                                it1, it.child("name").value.toString(),
                                it.child("email").value.toString(),
                                it.child("password").value.toString(),
                                it.child("birthDate").value.toString(),
                                it.child("gender").value.toString(),
                                it.child("photo").value.toString()
                            )
                        }
                        if (user != null) {
                            rowItems.add(user)
                        }
                        cardAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction == Direction.Right) {
            val likedUser = rowItems[manager.topPosition - 1]

            val pathToSymp = REFERENCE_DATABASE.child(NODE_USERS)
                .child(likedUser.id).child(NODE_SYMPATHIES)
            pathToSymp.child(likedUser.id)
                .child(CHILD_ID).setValue(likedUser.id)
        }
        //reload adapter
        if (cardAdapter.list.lastIndex == manager.topPosition - 1) {
            findAllUsers()
        }
    }

    override fun onCardRewound() {
        val lastLikedUser = rowItems[manager.topPosition]
        val path = REFERENCE_DATABASE.child(NODE_USERS)
            .child(lastLikedUser.id).child(NODE_SYMPATHIES).child(CURRENT_USER_ID)
        path.removeValue()
    }

    override fun onCardCanceled() {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }
}