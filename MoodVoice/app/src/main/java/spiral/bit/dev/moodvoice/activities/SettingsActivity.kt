package spiral.bit.dev.moodvoice.activities

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_settings.*
import spiral.bit.dev.moodvoice.R


class SettingsActivity : AppCompatActivity() {

    private lateinit var mainPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mainPrefs = this.getSharedPreferences("main_prefs", 0)
        initSpinners()
        initListeners()
        initOther()
        btn_save.setOnClickListener {
            if (et_name.text.isNotEmpty()) mainPrefs.edit().putString(
                "userName",
                et_name.text.toString()
            ).apply()
            Toast.makeText(this, "Изменения сохранены!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initOther() {
        val name = mainPrefs.getString("userName", "Пользователь")
        et_name.setText(name)
        btn_back.setOnClickListener { onBackPressed() }
    }

    private fun initSpinners() {
        val adapterMorningPushes: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(
                this,
                R.array.morning_pushes,
                android.R.layout.simple_spinner_item
            )
        adapterMorningPushes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_morning_pushes_hours.adapter = adapterMorningPushes

        val adapterMinutes: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(
                this,
                R.array.minutes,
                android.R.layout.simple_spinner_item
            )
        adapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_morning_pushes_minutes.adapter = adapterMinutes

        val adapterPeriod: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(
                this,
                R.array.periods,
                android.R.layout.simple_spinner_item
            )
        adapterPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_period.adapter = adapterPeriod
    }

    private fun initListeners() {
        spinner_morning_pushes_hours.onItemSelectedListener = object : OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                (parent!!.getChildAt(selectedItemPosition) as TextView).setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(this@SettingsActivity, R.drawable.drop_down_icon),
                    null
                )
                val choose = resources.getStringArray(R.array.morning_pushes)
                mainPrefs.edit().putInt(
                    "hour_pushes",
                    choose[selectedItemPosition].toInt()
                ).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinner_morning_pushes_minutes.onItemSelectedListener = object : OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                (parent!!.getChildAt(selectedItemPosition) as TextView).setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(this@SettingsActivity, R.drawable.drop_down_icon),
                    null
                )
                val choose = resources.getStringArray(R.array.minutes)
                mainPrefs.edit().putInt(
                    "minutes_pushes",
                    choose[selectedItemPosition].toInt()
                ).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinner_period.onItemSelectedListener = object : OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                (parent!!.getChildAt(selectedItemPosition) as TextView).setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(this@SettingsActivity, R.drawable.drop_down_icon),
                    null
                )
                val choose = resources.getStringArray(R.array.periods)
                mainPrefs.edit().putInt(
                    "period_pushes",
                    choose[selectedItemPosition].substring(0, 1).toInt()
                ).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}