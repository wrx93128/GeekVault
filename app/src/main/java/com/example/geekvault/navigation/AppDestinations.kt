package com.example.geekvault.navigation

/**
 * Single source of truth for every navigation route string in the GeekVault app.
 *
 * Use these constants in [AppNavigation], [BottomNavBar], [AppDrawer], and any
 * screen that calls [androidx.navigation.NavController.navigate] to ensure
 * route strings never go out of sync.
 */
object AppDestinations {
    const val LOGIN     = "login"
    const val REGISTER  = "register"
    const val HOME      = "home"
    const val FAVORITES = "favorites"
    const val PROFILE   = "profile"
    const val CONTACT   = "contact"
}
