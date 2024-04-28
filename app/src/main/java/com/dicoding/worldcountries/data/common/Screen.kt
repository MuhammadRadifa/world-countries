package com.dicoding.worldcountries.data.common

sealed class Screen(val route: String) {

    data object Home : Screen("Home")
    data object Favorite : Screen("Favorite")
    data object About : Screen("About")
    data object Detail : Screen("Detail")
}