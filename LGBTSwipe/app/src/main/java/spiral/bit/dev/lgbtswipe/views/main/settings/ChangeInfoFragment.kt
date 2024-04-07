package spiral.bit.dev.lgbtswipe.views.main.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentChangeInfoBinding
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.ProfileFragment

class ChangeInfoFragment : Fragment(R.layout.fragment_change_info) {

    private val changeInfoBinding: FragmentChangeInfoBinding by viewBinding(
        FragmentChangeInfoBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeInfoBinding.settingsToolbar.saveInfoImg.setOnClickListener {
            val about = changeInfoBinding.aboutChangeEt.text.toString()
            val name = changeInfoBinding.nameChangeEt.text.toString()
            val gender = changeInfoBinding.genderChangeEt.text.toString()
            val status = changeInfoBinding.statusChangeEt.text.toString()

            if (about.isNotEmpty()) {
                REFERENCE_DATABASE.child(NODE_USERS)
                    .child(CURRENT_USER_ID)
                    .child(CHILD_ABOUT_MOON)
                    .setValue(about)
            }
            if (name.isNotEmpty()) {
                REFERENCE_DATABASE.child(NODE_USERS)
                    .child(CURRENT_USER_ID)
                    .child(CHILD_NAME)
                    .setValue(name)
            }
            if (gender.isNotEmpty()) {
                REFERENCE_DATABASE.child(NODE_USERS)
                    .child(CURRENT_USER_ID)
                    .child(CHILD_GENDER_MOON)
                    .setValue(gender)
            }
            if (status.isNotEmpty()) {
                REFERENCE_DATABASE.child(NODE_USERS)
                    .child(CURRENT_USER_ID)
                    .child(CHILD_STATUS)
                    .setValue(status)
            }

            FancyToast.makeText(
                context, "Данные обновлены!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                false
            ).show()
            changeMainFragments(ProfileFragment(USER), false)
        }

        changeInfoBinding.settingsToolbar.cancelInfoImg.setOnClickListener {
            changeMainFragments(
                ProfileFragment(USER),
                false
            )
        }
    }
}