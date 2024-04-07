package spiral.bit.dev.lgbtswipe.views.reg_auth

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputEditText
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentGenderBinding
import spiral.bit.dev.lgbtswipe.other.changeFragments
import javax.inject.Inject

@AndroidEntryPoint
class GenderFragment : Fragment(R.layout.fragment_gender) {

    private val genderBinding: FragmentGenderBinding by viewBinding(FragmentGenderBinding::bind)

    @Inject
    lateinit var mainPrefs: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genderBinding.loginBtn.setOnClickListener {
            val emailFragment = EmailRegFragment()
            val bundle = Bundle()
            bundle.putBoolean("isLoginMode", true)
            emailFragment.arguments = bundle
            changeFragments(emailFragment, true)
        }

        genderBinding.btnNext.setOnClickListener {
            if (genderBinding.genderEt.text.isNullOrEmpty()) FancyToast.makeText(
                view.context, getString(R.string.enter_gender_toast),
                FancyToast.LENGTH_LONG,
                FancyToast.WARNING,
                false
            ).show()
            else {
                mainPrefs.edit().putString("gender", genderBinding.genderEt.text.toString()).apply()
                changeFragments(NameFragment(), true)
            }
        }
    }

    //        val waveView: MultiWaveHeader = view.findViewById(R.id.wave_header)
//        waveView.velocity = 1F
//        waveView.progress = 1F
//        waveView.isRunning
//        waveView.gradientAngle = 45
//        waveView.waveHeight = 40
//        waveView.startColor = Color.RED
//        waveView.closeColor = Color.CYAN
}