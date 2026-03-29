package com.example.geekvault.ui.main

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.geekvault.navigation.AppDestinations
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

private data class DrawerNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val drawerNavItems = listOf(
    DrawerNavItem(AppDestinations.HOME,      "Home",      Icons.Default.Home),
    DrawerNavItem(AppDestinations.FAVORITES, "Ulubione",  Icons.Default.Favorite),
    DrawerNavItem(AppDestinations.PROFILE,   "Profil",    Icons.Default.Person),
    DrawerNavItem(AppDestinations.CONTACT,   "Kontakt",   Icons.Default.Email)
)

/**
 * Content of the [ModalNavigationDrawer] used in [MainScaffold].
 *
 * Displays the currently signed-in user's email as a header, navigation items for
 * the three main sections, and a sign-out button at the bottom that clears the
 * entire back stack and returns to the login screen.
 *
 * @param navController The app-level [NavController] used for section navigation and sign-out.
 * @param drawerState   The shared [DrawerState] used to close the drawer after a tap.
 */
@Composable
fun AppDrawer(
    navController: NavController,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "Użytkownik"

    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = userEmail,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 28.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))

        drawerNavItems.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(item.route) {
                        popUpTo(AppDestinations.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        TextButton(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                scope.launch { drawerState.close() }
                navController.navigate(AppDestinations.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.padding(start = 12.dp, bottom = 16.dp)
        ) {
            Text(
                text = "Wyloguj",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
