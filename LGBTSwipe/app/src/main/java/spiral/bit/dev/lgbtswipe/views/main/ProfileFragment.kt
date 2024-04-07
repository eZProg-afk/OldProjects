package spiral.bit.dev.lgbtswipe.views.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentProfileBinding
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.profile.HorizontalSympAdapter
import spiral.bit.dev.lgbtswipe.views.main.settings.ChangeInfoFragment
import spiral.bit.dev.lgbtswipe.views.reg_auth.RegActivity
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment(var user: User) : Fragment(R.layout.fragment_profile) {

    private val profileBinding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    private var id: String = ""

    private val sympList = arrayListOf<User>()
    private val idsList = arrayListOf<String>()

    private lateinit var adapter: HorizontalSympAdapter

    @Inject
    lateinit var authClient: FirebaseAuth

    val path = REFERENCE_DATABASE.child(NODE_USERS).child(CURRENT_USER_ID)
        .child(NODE_SYMPATHIES)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MAIN_ACTIVITY.hideMenu(true)

        if (user.id == CURRENT_USER_ID) {
            id = CURRENT_USER_ID
            initRecycler()
            profileBinding.myProfileLabel.text = "Мой профиль"
            profileBinding.statusTv.text = "Статус:  ${user.status}"
        } else {
            id = user.id
            profileBinding.previewRecycler.visibility = View.GONE
            profileBinding.simpLabelTv.visibility = View.GONE
            profileBinding.settingsLabelTv.visibility = View.GONE
            profileBinding.myProfileLabel.text = "Профиль ${user.name}"
            profileBinding.sendMsgBtn.visibility = View.VISIBLE
            profileBinding.sendMsgBtn.setOnClickListener { changeMainFragments(SingleChatFragment(user), true) }
            profileBinding.sympBtn.visibility = View.GONE
            profileBinding.logOutBtn.visibility = View.GONE
            profileBinding.profileImg.isEnabled = false
            profileBinding.settingsBtn.visibility = View.GONE
            profileBinding.statusTv.text = "Статус:  ${user.status}"
        }

        val path = REFERENCE_STORAGE.child(AVATARS_USERS)
            .child(id)
        getURL(path) { profileBinding.profileImg.downloadAndSetImage(it) }

        profileBinding.nameTv.text = "Имя: ${user.name}"
        profileBinding.genderTv.text = "Гендер: ${user.gender}"
        profileBinding.statusTv.text = "Статус: ${user.status}"

        profileBinding.sympBtn.setOnClickListener {
            changeMainFragments(
                SympathiesFragment(),
                true
            )
        }

        profileBinding.settingsBtn.setOnClickListener { changeMainFragments(ChangeInfoFragment(), true) }

        profileBinding.profileImg.setOnClickListener {
            ImagePicker.create(this)
                .returnMode(ReturnMode.NONE)
                .folderMode(true)
                .toolbarFolderTitle("Изменение аватарки")
                .toolbarImageTitle("Выбрать картинку")
                .toolbarArrowColor(Color.WHITE)
                .single()
                .showCamera(true)
                .imageDirectory("Camera")
                .start()
        }

        profileBinding.logOutBtn.setOnClickListener {
            authClient.signOut()
            startActivity(Intent(context, RegActivity::class.java))
        }
    }

    private fun initRecycler() {
        adapter = HorizontalSympAdapter(sympList)
        profileBinding.previewRecycler.adapter = adapter
        getAllSympathies()
        profileBinding.previewRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        profileBinding.previewRecycler.setHasFixedSize(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val receiveImage: Image? = ImagePicker.getFirstImageOrNull(data)
            val path = REFERENCE_STORAGE.child(AVATARS_USERS)
                .child(id)
            context?.let {
                putFileInStorage(Uri.parse(receiveImage?.path), path) {
                    getURL(path) {
                        putUrlInDatabase(it) {
                            Toast.makeText(context, "Фото добавлено!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun getAllSympathies() {
        path.addValueEventListener(AppValueEventListener {
            for (data in it.children) {
                val listSnapValues = data.children
                for (i in listSnapValues) {
                    idsList.add(i.value.toString())
                }
            }

            for (j in idsList) {
                REFERENCE_DATABASE.child(NODE_USERS).child(j).get().addOnSuccessListener { snap ->
                    val user = snap.getValue(User::class.java)
                    if (user != null) {
                        if (!sympList.contains(user)) {
                            sympList.add(user)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }
}