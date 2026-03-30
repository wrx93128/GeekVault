package com.example.geekvault.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.geekvault.data.FavoriteCharacter
import com.example.geekvault.data.FavoriteDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoriteDao: FavoriteDao) : ViewModel() {

    val favorites: StateFlow<List<FavoriteCharacter>> = favoriteDao.getAllFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertFavorite(character: FavoriteCharacter) {
        viewModelScope.launch {
            favoriteDao.insertFavorite(character)
        }
    }

    fun deleteFavorite(character: FavoriteCharacter) {
        viewModelScope.launch {
            favoriteDao.deleteFavorite(character)
        }
    }

    fun updateNote(characterId: Int, note: String) {
        viewModelScope.launch {
            favoriteDao.updateNote(characterId, note)
        }
    }

    class Factory(private val favoriteDao: FavoriteDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
                return FavoritesViewModel(favoriteDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
