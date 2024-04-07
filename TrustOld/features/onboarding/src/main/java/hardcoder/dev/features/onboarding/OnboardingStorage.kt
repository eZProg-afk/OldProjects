package hardcoder.dev.features.onboarding

import hardcoder.dev.utils.common_resources.R
import hardcoder.dev.features.onboarding.presentation.adapters.ItemOnboarding

object OnboardingStorage {
    val onboardingItems = listOf(
        ItemOnboarding(
            picture = R.drawable.safety_image,
            title = R.string.safety_title,
            description = R.string.safety_description
        ),
        ItemOnboarding(
            picture = R.drawable.flexibility_image,
            title = R.string.flexibility_title,
            description = R.string.flexibility_description
        ),
        ItemOnboarding(
            picture = R.drawable.get_started_image,
            title = R.string.get_started_title,
            description = R.string.get_started_description
        )
    )
}