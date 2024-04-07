package spiral.bit.dev.runtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setup.*
import spiral.bit.dev.runtracker.R
import spiral.bit.dev.runtracker.other.Constants.KEY_FIRST_TIME_TOGGLE
import spiral.bit.dev.runtracker.other.Constants.KEY_NAME
import spiral.bit.dev.runtracker.other.Constants.KEY_WEIGHT
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isFirstAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }

        tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPrefs()
            if (success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(
                    requireView(),
                    "Пожалуйста, заполните все поля!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun writePersonalDataToSharedPrefs(): Boolean {
        val name = etName.text.toString()
        val weight = etWeight.text.toString()

        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPref.edit().putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()
        val toolbarText = "Вперёд, $name!"
        requireActivity().tvToolbarTitle.text = toolbarText
        return true
    }
}