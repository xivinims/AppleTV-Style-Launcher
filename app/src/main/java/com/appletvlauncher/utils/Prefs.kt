package com.appletvlauncher.utils

import android.content.Context
import android.content.SharedPreferences

object Prefs {
    private const val NAME = "apple_tv_launcher_prefs"
    private const val KEY_TIMEOUT = "screensaver_timeout_minutes"
    private const val KEY_COLUMNS = "grid_columns"
    private const val KEY_SHOW_NAMES = "show_app_names"
    private const val KEY_NIGHT_MODE = "night_mode_ios18"

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun getTimeoutMinutes(context: Context): Int {
        return prefs(context).getInt(KEY_TIMEOUT, 5)
    }

    fun setTimeoutMinutes(context: Context, minutes: Int) {
        prefs(context).edit().putInt(KEY_TIMEOUT, minutes).apply()
    }

    fun getColumns(context: Context): Int {
        return prefs(context).getInt(KEY_COLUMNS, 5)
    }

    fun setColumns(context: Context, columns: Int) {
        prefs(context).edit().putInt(KEY_COLUMNS, columns).apply()
    }

    fun showAppNames(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_SHOW_NAMES, true)
    }

    fun setShowAppNames(context: Context, show: Boolean) {
        prefs(context).edit().putBoolean(KEY_SHOW_NAMES, show).apply()
    }

    fun isNightMode(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_NIGHT_MODE, true)
    }

    fun setNightMode(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_NIGHT_MODE, enabled).apply()
    }
}
