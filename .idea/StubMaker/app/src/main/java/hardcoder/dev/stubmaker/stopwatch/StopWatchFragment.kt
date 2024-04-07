package hardcoder.dev.stubmaker.stopwatch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import hardcoder.dev.stubmaker.R
import hardcoder.dev.stubmaker.databinding.FragmentStopWatchBinding

class StopWatchFragment : Fragment(R.layout.fragment_stop_watch) {

    private val binding by viewBinding<FragmentStopWatchBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}