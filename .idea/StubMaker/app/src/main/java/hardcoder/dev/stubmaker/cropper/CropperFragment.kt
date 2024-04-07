package hardcoder.dev.stubmaker.cropper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import hardcoder.dev.stubmaker.R
import hardcoder.dev.stubmaker.databinding.FragmentCropperBinding

class CropperFragment : Fragment(R.layout.fragment_cropper) {

    private val binding by viewBinding<FragmentCropperBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}