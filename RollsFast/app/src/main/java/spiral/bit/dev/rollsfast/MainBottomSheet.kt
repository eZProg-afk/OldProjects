package spiral.bit.dev.rollsfast

import android.app.Dialog
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.bottom_sheet_main.*
import spiral.bit.dev.rollsfast.adapters.MainViewPagerAdapter

class MainBottomSheet : BottomSheetDialogFragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.bottom_sheet_main, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view: View = View.inflate(context, R.layout.bottom_sheet_main, layout_miscellaneous)
        bottomSheet.setContentView(view)
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        val display = activity?.windowManager?.defaultDisplay
        val point = Point()
        display?.getSize(point)
        val screenWidth: Int = point.y

        bottomSheetBehavior.peekHeight = screenWidth
//        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
//            override fun onStateChanged(view: View, i: Int) {
//                if (BottomSheetBehavior.STATE_EXPANDED == i) {
//                }
//
//                if (BottomSheetBehavior.STATE_COLLAPSED == i) {
//                }
//
//                if (BottomSheetBehavior.STATE_HIDDEN == i) dismiss()
//            }
//
//            override fun onSlide(view: View, v: Float) {}
//        })
        return bottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        main_view_pager.adapter = MainViewPagerAdapter(activity as AppCompatActivity)
        TabLayoutMediator(tabLayout, main_view_pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Роллы"
                }
                1 -> tab.text = "Суши"
                2 -> tab.text = "Сашими"
                3 -> tab.text = "Сеты"
                4 -> tab.text = "Вегетарианское"
                5 -> tab.text = "Напитки"
                else -> tab.text = "Роллы"
            }
        }.attach()
        tabLayout.isInlineLabel = true
        img_close_b_sheet.setOnClickListener { dismiss() }
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun showView(view: View, size: Int) {
        val params: ViewGroup.LayoutParams = view.layoutParams
        params.height = size
        view.layoutParams = params
    }
}