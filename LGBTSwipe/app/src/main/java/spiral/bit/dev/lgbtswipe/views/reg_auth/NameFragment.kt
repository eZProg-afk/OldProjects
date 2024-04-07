package spiral.bit.dev.lgbtswipe.views.reg_auth

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputEditText
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentNameBinding
import spiral.bit.dev.lgbtswipe.other.changeFragments
import javax.inject.Inject

@AndroidEntryPoint
class NameFragment : Fragment(R.layout.fragment_name) {

        private val nameBinding: FragmentNameBinding by viewBinding(FragmentNameBinding::bind)

    @Inject
    lateinit var mainPrefs: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameBinding.btnNext.setOnClickListener {
            if (nameBinding.etName.text.isNullOrEmpty()) FancyToast.makeText(
                view.context, getString(R.string.enter_your_name),
                FancyToast.LENGTH_LONG,
                FancyToast.WARNING,
                false
            ).show()
            else {
                mainPrefs.edit().putString("name", nameBinding.etName.text.toString()).apply()
                changeFragments(BirthFragment(), true)
            }
        }
    }
}