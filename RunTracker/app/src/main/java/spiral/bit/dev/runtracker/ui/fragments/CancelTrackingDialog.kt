package spiral.bit.dev.runtracker.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import spiral.bit.dev.runtracker.R

class CancelTrackingDialog : DialogFragment() {

    private var yesListener: (() -> Unit)? = null

    fun setYesListener(listener: () -> Unit) {
        yesListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Окончание пробежки")
            .setMessage("Вы уверены в том, что хотите закончить текущую пробежку?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Да, уверен/а") { _, _ ->
                yesListener?.let { yes ->
                    yes()
                }
            }
            .setNegativeButton("Нет") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
    }
}