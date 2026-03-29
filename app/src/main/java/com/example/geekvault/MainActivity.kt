package com.example.geekvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.geekvault.navigation.AppNavigation
import com.example.geekvault.ui.theme.GeekVaultTheme

/**
 * Single entry-point Activity for GeekVault.
 *
 * Installs the splash screen before calling [super.onCreate], then hands all
 * UI rendering to [AppNavigation] wrapped in [GeekVaultTheme].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeekVaultTheme {
                AppNavigation()
            }
        }
    }
}