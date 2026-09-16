package com.appletvlauncher.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appletvlauncher.R
import com.appletvlauncher.model.AppInfo
import com.appletvlauncher.utils.Prefs
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var clockText: TextView
    private lateinit var appsGrid: RecyclerView
    private lateinit var settingsButton: ImageButton
    private lateinit var backgroundImage: ImageView

    private val clockHandler = Handler(Looper.getMainLooper())
    private val idleHandler = Handler(Looper.getMainLooper())
    private var idleRunnable: Runnable? = null

    private val clockRunnable = object : Runnable {
        override fun run() {
            updateClock()
            clockHandler.postDelayed(this, 30_000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clockText = findViewById(R.id.clockText)
        appsGrid = findViewById(R.id.appsGrid)
        settingsButton = findViewById(R.id.settingsButton)
        backgroundImage = findViewById(R.id.backgroundImage)

        setupClock()
        setupGrid()
        setupSettingsButton()
        applyNightMode()
        resetIdleTimer()
    }

    private fun setupClock() {
        updateClock()
        clockHandler.post(clockRunnable)
    }

    private fun updateClock() {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        clockText.text = format.format(Date())
    }

    private fun setupGrid() {
        val apps = loadInstalledApps()
        val columns = Prefs.getColumns(this)

        appsGrid.layoutManager = GridLayoutManager(this, columns)
        appsGrid.adapter = AppAdapter(apps) { app ->
            app.launchIntent?.let {
                try {
                    startActivity(it)
                } catch (e: Exception) {
                }
            }
            resetIdleTimer()
        }

        appsGrid.post {
            if (apps.isNotEmpty()) {
                appsGrid.findViewHolderForAdapterPosition(0)?.itemView?.requestFocus()
            }
        }
    }

    private fun loadInstalledApps(): List<AppInfo> {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }
        val mobileIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val apps = mutableListOf<AppInfo>()
        val seen = mutableSetOf<String>()

        fun addFromResolve(list: List<android.content.pm.ResolveInfo>) {
            for (ri in list) {
                val pkg = ri.activityInfo.packageName
                if (pkg == packageName || seen.contains(pkg)) continue
                seen.add(pkg)
                try {
                    val ai = pm.getApplicationInfo(pkg, 0)
                    val label = pm.getApplicationLabel(ai).toString()
                    val icon = pm.getApplicationIcon(ai)
                    val launch = pm.getLeanbackLaunchIntentForPackage(pkg)
                        ?: pm.getLaunchIntentForPackage(pkg)
                    apps.add(AppInfo(pkg, label, icon, launch))
                } catch (_: Exception) {
                }
            }
        }

        addFromResolve(pm.queryIntentActivities(intent, PackageManager.MATCH_ALL))
        addFromResolve(pm.queryIntentActivities(mobileIntent, PackageManager.MATCH_ALL))

        return apps.sortedBy { it.label.lowercase(Locale.getDefault()) }
    }

    private fun setupSettingsButton() {
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            resetIdleTimer()
        }
        settingsButton.setOnFocusChangeListener { v, hasFocus ->
            v.animate().scaleX(if (hasFocus) 1.2f else 1f).scaleY(if (hasFocus) 1.2f else 1f)
                .setDuration(150).start()
        }
    }

    private fun applyNightMode() {
        // Tema já é dark. Night mode iOS 18 está ativo por padrão.
    }

    private fun resetIdleTimer() {
        idleRunnable?.let { idleHandler.removeCallbacks(it) }
        val timeoutMs = Prefs.getTimeoutMinutes(this) * 60_000L
        idleRunnable = Runnable {
            startActivity(Intent(this, ScreensaverActivity::class.java))
        }
        idleHandler.postDelayed(idleRunnable!!, timeoutMs)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        resetIdleTimer()
    }

    override fun onResume() {
        super.onResume()
        resetIdleTimer()
        setupGrid()
    }

    override fun onPause() {
        super.onPause()
        idleRunnable?.let { idleHandler.removeCallbacks(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        clockHandler.removeCallbacks(clockRunnable)
        idleRunnable?.let { idleHandler.removeCallbacks(it) }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        resetIdleTimer()
        return super.onKeyDown(keyCode, event)
    }
}
