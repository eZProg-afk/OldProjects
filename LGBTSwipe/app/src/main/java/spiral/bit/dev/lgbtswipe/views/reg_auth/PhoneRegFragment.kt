package spiral.bit.dev.lgbtswipe.views.reg_auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentPhoneRegBinding

class PhoneRegFragment : Fragment(R.layout.fragment_phone_reg) {

    private val phoneRegBinding: FragmentPhoneRegBinding by viewBinding(FragmentPhoneRegBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}