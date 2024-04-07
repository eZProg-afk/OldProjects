package spiral.bit.dev.lgbtswipe.views.reg_auth

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentBirthBinding
import spiral.bit.dev.lgbtswipe.databinding.FragmentGenderBinding
import spiral.bit.dev.lgbtswipe.other.changeFragments
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BirthFragment : Fragment(R.layout.fragment_birth), DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var calendar: Calendar

    @Inject
    lateinit var calendarYear: Calendar

    @Inject
    lateinit var sdf: DateFormat

    @Inject
    lateinit var mainPrefs: SharedPreferences

    private val birthBinding: FragmentBirthBinding by viewBinding(FragmentBirthBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        birthBinding.selectDateTimeTv.setOnClickListener {
            val datePickerDialog = showDatePickerDialog(view.context)
            datePickerDialog.show()
        }
        birthBinding.btnNext.setOnClickListener {
            changeFragments(fragment = PhotoFragment(), addStack = true)
        }
    }

    private fun showDatePickerDialog(context: Context): DatePickerDialog {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(context, this, year, month, day)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        if (calendarYear.get(Calendar.YEAR) - 18 != year) {
            FancyToast.makeText(
                context, getString(R.string.you_not_18_years_old_toast),
                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false
            )
                .show()
        } else {
            val currDateString = sdf.format(calendar.time)
            mainPrefs.edit().putString("birthDate", currDateString).apply()
        }
    }
}