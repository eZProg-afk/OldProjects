package spiral.bit.dev.rollsfast.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import spiral.bit.dev.rollsfast.fragments.*

class MainViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> RollsFragment()
        1 -> SushiFragment()
        2 -> SashimiFragment()
        3 -> SetsFragment()
        4 -> VegetableFragment()
        5 -> DrinksFragment()
        else -> RollsFragment()
    }
}