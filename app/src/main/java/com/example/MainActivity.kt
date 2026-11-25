package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.firebase.configureAppCheck
import com.example.ui.SignalTerminalScreen
import com.example.ui.theme.FxSignalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Configure Firebase App Check with launch intent before any UI or ViewModels
        configureAppCheck(this, intent)

        // STEP 2: Enable full-bleed dark edge-to-edge rendering
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        // STEP 3: Render Compose UI
        setContent {
            FxSignalTheme {
                SignalTerminalScreen()
            }
        }
    }
}
