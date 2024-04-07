package hardcoder.dev.features.onboarding.presentation.adapters

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import hardcoder.dev.common_base.base.ListItem
import hardcoder.dev.features.onboarding.databinding.ItemOnboardingContainerBinding

open class ItemOnboarding(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val picture: Int
) : ListItem

val onboardingDelegate =
    adapterDelegateViewBinding<ItemOnboarding, ItemOnboarding, ItemOnboardingContainerBinding>({ inflater, container ->
        ItemOnboardingContainerBinding.inflate(inflater, container, false)
    }) {
        with(binding) {
            bind {
                titleTextView.text = getString(item.title)
                descriptionTextView.text = getString(item.description)
                onboardingPictureImageView.setImageResource(item.picture)
            }
        }
    }



