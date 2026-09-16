package com.appletvlauncher.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appletvlauncher.R
import com.appletvlauncher.model.AppInfo
import com.appletvlauncher.utils.Prefs

class AppAdapter(
    private val apps: List<AppInfo>,
    private val onAppClick: (AppInfo) -> Unit
) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.appIcon)
        val name: TextView = itemView.findViewById(R.id.appName)
        val container: View = itemView.findViewById(R.id.appContainer)

        init {
            itemView.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onAppClick(apps[pos])
                }
            }

            itemView.setOnFocusChangeListener { v, hasFocus ->
                val scale = if (hasFocus) 1.15f else 1.0f
                v.animate()
                    .scaleX(scale)
                    .scaleY(scale)
                    .setDuration(180)
                    .setInterpolator(DecelerateInterpolator())
                    .start()

                if (hasFocus) {
                    icon.elevation = 24f
                } else {
                    icon.elevation = 8f
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]
        holder.icon.setImageDrawable(app.icon)
        holder.name.text = app.label

        val showNames = Prefs.showAppNames(holder.itemView.context)
        holder.name.visibility = if (showNames) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = apps.size
}
