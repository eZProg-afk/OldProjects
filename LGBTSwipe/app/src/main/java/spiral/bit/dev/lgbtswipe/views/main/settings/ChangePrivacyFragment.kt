package spiral.bit.dev.lgbtswipe.views.main.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentChangePrivacyBinding

class ChangePrivacyFragment : Fragment(R.layout.fragment_change_privacy) {

    private val changePrivacyBinding: FragmentChangePrivacyBinding by viewBinding(FragmentChangePrivacyBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}