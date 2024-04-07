package spiral.bit.dev.runtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import spiral.bit.dev.runtracker.R
import spiral.bit.dev.runtracker.other.Constants.KEY_NAME
import spiral.bit.dev.runtracker.other.Constants.KEY_WEIGHT
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFieldsFromSharedPref()
        btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()
            if (success) {
                Snackbar.make(
                    view,
                    "Изменения сохранены!",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    view,
                    "Пожалуйста, заполните все поля!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loadFieldsFromSharedPref() {
        val name = sharedPref.getString(KEY_NAME, "")
        val weight = sharedPref.getFloat(KEY_WEIGHT, 80f)
        etName.setText(name.toString())
        etWeight.setText(weight.toString())
    }

    private fun applyChangesToSharedPref(): Boolean {
        val nameText = etName.text.toString()
        val weightText = etWeight.text.toString()

        if (nameText.isEmpty() || weightText.isEmpty()) return false
        sharedPref.edit()
            .putString(KEY_NAME, nameText)
            .putFloat(KEY_WEIGHT, weightText.toFloat())
            .apply()
        val toolbarText = "Вперёд, $nameText"
        requireActivity().tvToolbarTitle.text = toolbarText
        return true
    }
}