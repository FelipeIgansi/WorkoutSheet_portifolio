package com.trainsmart.systemsettings

import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

class ConfigSystemUIBar(private var window: Window) {
    fun alteraCorBarraSuperior() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightStatusBars = false // Para texto branco na barra de status
            insets
        }
    }
}
