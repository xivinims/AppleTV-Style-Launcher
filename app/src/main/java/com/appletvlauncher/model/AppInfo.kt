package com.appletvlauncher.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val launchIntent: android.content.Intent?
)
