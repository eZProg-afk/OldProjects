package hardcoder.dev.stubmaker.photoEditor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import hardcoder.dev.stubmaker.R
import hardcoder.dev.stubmaker.databinding.FragmentPhotoEditorBinding

class PhotoEditorFragment : Fragment(R.layout.fragment_photo_editor) {

    private val binding by viewBinding<FragmentPhotoEditorBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}