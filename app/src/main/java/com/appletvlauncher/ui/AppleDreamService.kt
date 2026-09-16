package com.appletvlauncher.ui

import android.service.dreams.DreamService
import android.widget.TextView
import com.appletvlauncher.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppleDreamService : DreamService() {

    private var clockView: TextView? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isInteractive = false
        isFullscreen = true
        setContentView(R.layout.activity_screensaver)
        clockView = findViewById(R.id.screensaverClock)
        updateClock()
    }

    private fun updateClock() {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        clockView?.text = format.format(Date())
    }
}
