package com.dicoding.worldcountries.ui.screen.favorite

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.worldcountries.data.network.local.FavoriteEntity
import com.dicoding.worldcountries.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository):ViewModel() {

    private val _country = mutableStateOf("")
    val countryState = _country.value

    fun addCountry(country: String){
        _country.value = country
    }

    fun addFavorite(favoriteEntity: FavoriteEntity){
        viewModelScope.launch {
            favoriteRepository.addFavorite(favoriteEntity)
        }
    }

    fun deleteFavorite(favoriteEntity: FavoriteEntity){
        viewModelScope.launch {
            favoriteRepository.deleteFavoriteByName(favoriteEntity)
        }
    }

    fun getAllFavorites():Flow<List<FavoriteEntity>> = favoriteRepository.getAllFavorites()

    fun getFavoriteByName(name: String):Flow<FavoriteEntity> = favoriteRepository.getFavoriteByName(name)
}