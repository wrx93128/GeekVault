package com.example.geekvault.ui.auth

/**
 * Represents the possible UI states for authentication screens (login, register).
 */
sealed class AuthUiState {

    /** No operation in progress; screen is ready for user input. */
    object Idle : AuthUiState()

    /** An authentication request has been sent and a response is awaited. */
    object Loading : AuthUiState()

    /** Authentication completed successfully. */
    object Success : AuthUiState()

    /**
     * Authentication failed with the given [message].
     *
     * @param message Human-readable description of the failure provided by Firebase.
     */
    data class Error(val message: String) : AuthUiState()
}
