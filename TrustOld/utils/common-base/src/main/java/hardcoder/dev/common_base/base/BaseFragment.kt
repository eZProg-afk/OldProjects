package hardcoder.dev.common_base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.util.concurrent.TimeUnit

abstract class BaseFragment<BINDING : ViewBinding>(
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> BINDING
) : Fragment() {

    protected var binding: BINDING? = null
        private set

    private fun binding() = checkNotNull(binding)

    fun binding(setup: BINDING.() -> Unit) = binding().let(setup)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflateBinding(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition(POSTPONE_TIMEOUT, TimeUnit.MILLISECONDS)
        checkNotNull(binding).onCreated()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    open fun BINDING.onCreated() = Unit

    private companion object {
        private const val POSTPONE_TIMEOUT = 200L
    }
}