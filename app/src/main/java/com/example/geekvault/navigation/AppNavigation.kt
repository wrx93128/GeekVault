package com.example.geekvault.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geekvault.data.AppDatabase
import com.example.geekvault.ui.auth.AuthViewModel
import com.example.geekvault.ui.auth.LoginScreen
import com.example.geekvault.ui.auth.RegisterScreen
import com.example.geekvault.ui.contact.ContactScreen
import com.example.geekvault.ui.favorites.FavoritesScreen
import com.example.geekvault.ui.favorites.FavoritesViewModel
import com.example.geekvault.ui.home.HomeScreen
import com.example.geekvault.ui.main.MainScaffold
import com.google.firebase.auth.FirebaseAuth

/**
 * Root navigation graph for the entire GeekVault application.
 *
 * Determines the start destination based on the current Firebase auth state —
 * users already signed in land directly on [AppDestinations.HOME], others start
 * at [AppDestinations.LOGIN].
 *
 * Auth destinations (login, register) are rendered without any chrome.
 * Main destinations (home, favorites, profile) are wrapped inside [MainScaffold]
 * which provides the top bar, bottom nav bar, and navigation drawer.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        AppDestinations.HOME
    } else {
        AppDestinations.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ── Auth routes — no chrome ──────────────────────────────────────────

        composable(AppDestinations.LOGIN) {
            val viewModel: AuthViewModel = viewModel()
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(AppDestinations.REGISTER) {
            val viewModel: AuthViewModel = viewModel()
            RegisterScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // ── Main routes — wrapped in MainScaffold ───────────────────────────

        composable(AppDestinations.HOME) {
            val database = AppDatabase.getDatabase(context)
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModel.Factory(database.favoriteDao())
            )
            MainScaffold(navController = navController) {
                HomeScreen(
                    onFavoriteClick = { favoriteCharacter ->
                        favoritesViewModel.insertFavorite(favoriteCharacter)
                    }
                )
            }
        }

        composable(AppDestinations.FAVORITES) {
            val database = AppDatabase.getDatabase(context)
            val viewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModel.Factory(database.favoriteDao())
            )
            MainScaffold(navController = navController) {
                FavoritesScreen(viewModel = viewModel)
            }
        }

        composable(AppDestinations.PROFILE) {
            MainScaffold(navController = navController) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Profil")
                }
            }
        }

        composable(AppDestinations.CONTACT) {
            MainScaffold(navController = navController) {
                ContactScreen()
            }
        }
    }
}
