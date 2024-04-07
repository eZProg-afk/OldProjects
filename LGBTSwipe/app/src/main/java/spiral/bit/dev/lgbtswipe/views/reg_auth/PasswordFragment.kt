package spiral.bit.dev.lgbtswipe.views.reg_auth

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputEditText
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentPasswordBinding
import spiral.bit.dev.lgbtswipe.other.changeFragments
import javax.inject.Inject

@AndroidEntryPoint
class PasswordFragment : Fragment(R.layout.fragment_password) {

    private val passwordBinding: FragmentPasswordBinding by viewBinding(FragmentPasswordBinding::bind)

    @Inject lateinit var mainPrefs: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordBinding.btnNext.setOnClickListener {
            if (passwordBinding.etPassword.text.isNullOrEmpty()) FancyToast.makeText(
                view.context, getString(R.string.create_password_toast),
                FancyToast.LENGTH_LONG,
                FancyToast.WARNING,
                false).show()
            else {
                mainPrefs.edit().putString("password", passwordBinding.etPassword.text.toString()).apply()
                changeFragments(EmailRegFragment(), true)
            }
        }
    }
}