package com.dicoding.worldcountries.di

import com.dicoding.worldcountries.repository.FavoriteRepository
import com.dicoding.worldcountries.ui.screen.favorite.FavoriteViewModel
import com.dicoding.worldcountries.ui.screen.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { FavoriteRepository(get()) }

    viewModel { HomeViewModel() }
    viewModel { FavoriteViewModel(get()) }
}