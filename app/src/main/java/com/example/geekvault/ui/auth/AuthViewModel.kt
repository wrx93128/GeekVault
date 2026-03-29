package com.example.geekvault.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel responsible for Firebase Authentication operations.
 *
 * Exposes [uiState] as a [StateFlow] of [AuthUiState] so the UI can react to
 * Idle, Loading, Success, and Error conditions without coupling this class to
 * any Compose runtime.
 */
class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)

    /** Read-only stream of authentication UI state updates. */
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /**
     * Attempts to sign in an existing user with the provided credentials.
     *
     * Sets [uiState] to [AuthUiState.Loading] immediately, then transitions to
     * [AuthUiState.Success] on a successful sign-in or [AuthUiState.Error] with
     * the Firebase exception message on failure.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     */
    fun login(email: String, password: String) {
        _uiState.value = AuthUiState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _uiState.value = AuthUiState.Success
            }
            .addOnFailureListener { exception ->
                _uiState.value = AuthUiState.Error(
                    exception.message ?: "Login failed. Please try again."
                )
            }
    }

    /**
     * Creates a new Firebase user account with the provided credentials.
     *
     * Sets [uiState] to [AuthUiState.Loading] immediately, then transitions to
     * [AuthUiState.Success] on successful account creation or [AuthUiState.Error]
     * with the Firebase exception message on failure.
     *
     * @param email    The email address for the new account.
     * @param password The password for the new account.
     */
    fun register(email: String, password: String) {
        _uiState.value = AuthUiState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _uiState.value = AuthUiState.Success
            }
            .addOnFailureListener { exception ->
                _uiState.value = AuthUiState.Error(
                    exception.message ?: "Registration failed. Please try again."
                )
            }
    }

    /**
     * Resets [uiState] back to [AuthUiState.Idle].
     *
     * Call this after the UI has consumed a [AuthUiState.Success] or
     * [AuthUiState.Error] event to prevent it from being re-processed on
     * recomposition or navigation.
     */
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
