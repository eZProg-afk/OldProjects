package spiral.bit.dev.lgbtswipe.views.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentAccountInfoBinding
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.other.*

class AccountInfoFragment(private val mReceivingUser: User) :
    Fragment(R.layout.fragment_account_info) {

    private val accInfoBinding: FragmentAccountInfoBinding by viewBinding(FragmentAccountInfoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accInfoBinding.userFullName.text = mReceivingUser.name
        if (REFERENCE_DATABASE.child(NODE_USERS).child(mReceivingUser.id).child(
                mReceivingUser.closedProfile.toString()
            ).key == "CLOSED"
        ) {
            val path = mReceivingUser.id.let { REFERENCE_STORAGE.child(AVATARS_USERS).child(it) }
            getURL(path) { accInfoBinding.userPhotoAva.downloadAndSetImage(it) }
            //accInfoBinding.userPhotoAva.setOnClickListener { showFullImageWindow() }
            accInfoBinding.userStatus.text = "Информация скрыта"
            accInfoBinding.userPhone.text = "Информация скрыта"
            accInfoBinding.userAbout.text = "Информация скрыта"
        } else {
            val path = mReceivingUser.id.let { REFERENCE_STORAGE.child(AVATARS_USERS).child(it) }
            getURL(path) { accInfoBinding.userPhotoAva.downloadAndSetImage(it) }
            //accInfoBinding.userPhotoAva.setOnClickListener { showFullImageWindow() }
            accInfoBinding.userStatus.text = mReceivingUser.status
            // accInfoBinding.userPhone.text = mReceivingUser.phone
            accInfoBinding.userAbout.text = mReceivingUser.about
        }
    }

//    private fun showFullImageWindow() {
//        val dialog: AlertDialog.Builder = AlertDialog.Builder(ACTIVITY)
//        val inflater: LayoutInflater = LayoutInflater.from(ACTIVITY)
//        val fullImageWindow: View = inflater.inflate(R.layout.full_image_window, null)
//        dialog.setTitle("Аватарка ${user_full_name.text}")
//        dialog.setView(fullImageWindow)
//        fullImageWindow.full_image.downloadAndSetImage(mReceivingUser.photo)
//        dialog.setNegativeButton(
//            "Закрыть"
//        ) { dialogInterface, which ->
//            dialogInterface.dismiss()
//        }
//        dialog.show()
//    }
}