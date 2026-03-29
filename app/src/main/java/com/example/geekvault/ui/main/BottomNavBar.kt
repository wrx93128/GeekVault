package com.example.geekvault.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.geekvault.navigation.AppDestinations

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(AppDestinations.HOME,      "Home",      Icons.Default.Home),
    BottomNavItem(AppDestinations.FAVORITES, "Ulubione",  Icons.Default.Favorite),
    BottomNavItem(AppDestinations.PROFILE,   "Profil",    Icons.Default.Person)
)

/**
 * Material3 bottom navigation bar for the three main sections of GeekVault.
 *
 * Highlights the item matching the currently active back-stack destination.
 * Uses [launchSingleTop] and [restoreState] so tapping the same tab does not
 * create duplicate entries and state is preserved when switching tabs.
 *
 * @param navController The app-level [NavController] used for tab navigation.
 */
@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                label = { Text(item.label) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(AppDestinations.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
