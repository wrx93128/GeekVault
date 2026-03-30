package com.example.geekvault.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geekvault.data.model.Character
import com.example.geekvault.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val characters: List<Character>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel : ViewModel() {

    var uiState by mutableStateOf<HomeUiState>(HomeUiState.Loading)
        private set

    init { fetchCharacters() }

    fun fetchCharacters() {
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            uiState = try {
                val response = RetrofitInstance.api.getCharacters()
                HomeUiState.Success(response.results)
            } catch (e: Exception) {
                HomeUiState.Error("Brak połączenia z siecią")
            }
        }
    }
}
