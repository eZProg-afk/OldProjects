package com.example.minicoder

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.minicoder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mainBinding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainBinding.imageAddNew.setOnClickListener {

        }


    }

    private fun openAddViewDialog() {
        val builder = AlertDialog.Builder(this)
        val view: View = LayoutInflater.from(this)
            .inflate(
                R.layout.create_view_dialog,
                findViewById<View>(R.id.create_view_dialog_container) as ViewGroup
            )
        builder.setView(view)
        val dialogAddView = builder.create()
        if (dialogAddView.window != null) dialogAddView.window!!.setBackgroundDrawable(ColorDrawable(0))
        view.findViewById<View>(R.id.text_cancel).setOnClickListener {
            dialogAddView.dismiss()
        }
        view.findViewById<View>(R.id.text_add).setOnClickListener {

        }
        dialogAddView.show()
    }
}