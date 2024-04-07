package hardcoder.dev.stubmaker.calculator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import hardcoder.dev.stubmaker.R
import hardcoder.dev.stubmaker.databinding.FragmentCalculatorStubBinding

class CalculatorStubFragment : Fragment(R.layout.fragment_calculator_stub) {

    private val binding by viewBinding<FragmentCalculatorStubBinding>()
    private val numTextViews by lazy {
        with(binding) {
            listOf(
                oneTextView,
                twoTextView,
                threeTextView,
                fourTextView,
                fiveTextView,
                sixTextView,
                sevenTextView,
                eightTextView,
                nineTextView
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
    }

    private fun setUpClicks() = with(binding) {
        numTextViews.forEach { button ->
            button.setOnClickListener {  }
        }
    }
}