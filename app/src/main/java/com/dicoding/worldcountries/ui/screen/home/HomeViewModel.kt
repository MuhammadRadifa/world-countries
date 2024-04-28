package com.dicoding.worldcountries.ui.screen.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.worldcountries.data.network.remote.ApiConfig

import com.dicoding.worldcountries.data.network.remote.DataItem
import com.dicoding.worldcountries.data.state.ResultState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    data class ResponseState<T>(
        val loading: Boolean = true,
        val list: List<T> = emptyList(),
        val error: String? = null
    )

    private val _tempResourceCountries = mutableStateOf(ResponseState<DataItem>())
    private val _countries = mutableStateOf(ResponseState<DataItem>())
    val countries = _countries

    private val _city = mutableStateOf(ResponseState<String>())
    val city = _city

    init {
        getAllCountries()
    }

    private fun getAllCountries() {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getAllCountries()
                _countries.value = _countries.value.copy(
                    loading = false,
                    list = response.data,
                    error = null
                )
                _tempResourceCountries.value = _tempResourceCountries.value.copy(
                    list = response.data,
                )
            } catch (e: Exception) {
                Log.e("HomeViewModel", "getAllCountries: ${e}", e)
                _countries.value = _countries.value.copy(
                    loading = false,
                    list = emptyList(),
                    error = e.message
                )
            }
        }
    }

    fun getCityByCountry(country: String) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getCityByCountry(country)
                _city.value = _city.value.copy(
                    loading = false,
                    list = response.data,
                    error = null
                )
            } catch (e: Exception) {
                Log.e("HomeViewModel", "getCityByCountry: ${e}", e)
                _city.value = _city.value.copy(
                    loading = false,
                    list = emptyList(),
                    error = e.message
                )
            }
        }
    }



    fun searchCountry(query: String) {
        val list = _tempResourceCountries.value.list
        val searchResult = list.filter {
            it.name.contains(query, ignoreCase = true)
        }
        _countries.value = _countries.value.copy(
            list = searchResult,
            error = if (searchResult.isEmpty()) "No data found" else null
        )
    }

}