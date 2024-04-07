package hardcoder.dev.features.onboarding.presentation

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import hardcoder.dev.common_base.base.BaseFragment
import hardcoder.dev.common_base.di.BaseModule
import hardcoder.dev.common_base.extensions.launchWith
import hardcoder.dev.common_base.extensions.navigateToDeepLink
import hardcoder.dev.features.onboarding.OnboardingStorage
import hardcoder.dev.features.onboarding.databinding.FragmentOnboardingBinding
import hardcoder.dev.features.onboarding.di.DaggerOnboardingComponent
import hardcoder.dev.features.onboarding.presentation.adapters.onboardingDelegate
import hardcoder.dev.features.onboarding.presentation.adapters.onboardingDiffCallback
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class OnboardingFragment :
    BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<OnboardingViewModel> { factory }
    private val adapter = AsyncListDifferDelegationAdapter(onboardingDiffCallback, onboardingDelegate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDI()
        setUpViewpager()
        setUpIndicators()
        setUpClicks()
        subscribeToObservables()
    }

    private fun setUpDI() {
        DaggerOnboardingComponent
            .builder()
            .baseModule(BaseModule(requireActivity().application))
            .build()
            .inject(this)
    }

    private fun subscribeToObservables() = binding {
        lifecycleScope.launchWhenResumed {
            viewModel.uiState.onEach { (currentItemPosition, isFinishedReading) ->
                if (isFinishedReading) {
                    navigateToDeepLink(viewModel.buildDeepLinkToSetUpFlow())
                } else {
                    onboardingViewPager.setCurrentItem(currentItemPosition, true)
                }
            }.launchWith(viewLifecycleOwner)

            viewModel.onboardingItems.onEach { onboardingItems ->
                adapter.items = onboardingItems
            }.launchWith(viewLifecycleOwner)
        }
    }

    private fun setUpClicks() = binding {
        iconNextImageView.setOnClickListener {
            viewModel.toNextPage()
        }

        skipTextView.setOnClickListener {
            viewModel.skipOnboarding()
        }

        getStartedButton.setOnClickListener {
            viewModel.skipOnboarding()
        }
    }

    private fun setUpViewpager() = binding {
        onboardingViewPager.adapter = adapter
        onboardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.pageManually(position)
                setCurrentIndicator(position)
            }
        })
        onboardingViewPager.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setCurrentIndicator(position: Int) = binding {
        val childCount = containerIndicatorsLinearLayout.childCount
        for (i in 0 until childCount) {
            val imageView = containerIndicatorsLinearLayout.getChildAt(i) as ImageView
            val indicatorIcon = viewModel.getIndicatorIcon(i == position)
            imageView.setImageDrawable(indicatorIcon)
        }
    }

    private fun setUpIndicators() = binding {
        val indicators = arrayOfNulls<ImageView>(OnboardingStorage.onboardingItems.size) // TODO
        val layoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { setMargins(8, 0, 8, 0) }
        for (index in (indicators.indices)) {
            indicators[index] = ImageView(requireContext())
            indicators[index]?.apply {
                setImageDrawable(viewModel.getIndicatorIcon(isActive = false))
                setLayoutParams(layoutParams)
                containerIndicatorsLinearLayout.addView(this)
            }
        }

        setCurrentIndicator(0)
    }
}