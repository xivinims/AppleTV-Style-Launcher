package com.appletvlauncher.ui

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.appletvlauncher.R
import com.appletvlauncher.utils.Prefs

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val timeoutSeek = findViewById<SeekBar>(R.id.timeoutSeek)
        val timeoutValue = findViewById<TextView>(R.id.timeoutValue)
        val columnsSeek = findViewById<SeekBar>(R.id.columnsSeek)
        val columnsValue = findViewById<TextView>(R.id.columnsValue)
        val showNamesSwitch = findViewById<Switch>(R.id.showNamesSwitch)
        val nightModeSwitch = findViewById<Switch>(R.id.nightModeSwitch)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val backButton = findViewById<Button>(R.id.backButton)

        val currentTimeout = Prefs.getTimeoutMinutes(this)
        timeoutSeek.progress = currentTimeout
        timeoutValue.text = "$currentTimeout min"

        val currentColumns = Prefs.getColumns(this)
        columnsSeek.progress = currentColumns - 3
        columnsValue.text = "$currentColumns colunas"

        showNamesSwitch.isChecked = Prefs.showAppNames(this)
        nightModeSwitch.isChecked = Prefs.isNightMode(this)

        timeoutSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = progress.coerceAtLeast(1)
                timeoutValue.text = "$value min"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        columnsSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = (progress + 3).coerceIn(3, 7)
                columnsValue.text = "$value colunas"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        saveButton.setOnClickListener {
            Prefs.setTimeoutMinutes(this, timeoutSeek.progress.coerceAtLeast(1))
            Prefs.setColumns(this, (columnsSeek.progress + 3).coerceIn(3, 7))
            Prefs.setShowAppNames(this, showNamesSwitch.isChecked)
            Prefs.setNightMode(this, nightModeSwitch.isChecked)
            finish()
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}
