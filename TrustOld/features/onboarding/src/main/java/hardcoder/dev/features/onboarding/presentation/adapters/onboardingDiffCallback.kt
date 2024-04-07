package hardcoder.dev.features.onboarding.presentation.adapters

import androidx.recyclerview.widget.DiffUtil

val onboardingDiffCallback = object : DiffUtil.ItemCallback<ItemOnboarding>() {
    override fun areItemsTheSame(oldItem: ItemOnboarding, newItem: ItemOnboarding) = oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: ItemOnboarding, newItem: ItemOnboarding) = oldItem == newItem
}