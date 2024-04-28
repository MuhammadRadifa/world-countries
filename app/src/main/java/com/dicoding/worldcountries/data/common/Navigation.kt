package com.dicoding.worldcountries.data.common

import com.dicoding.worldcountries.R

data class NavigationItem(
    val title:String,
    val icon:Int,
)

val itemNavigation = listOf<NavigationItem>(
    NavigationItem(
        title = "Favorite",
        icon = R.drawable.baseline_favorite_24
    ),
    NavigationItem(
        title = "WhiteSpace",
        icon = R.drawable.carbon_earth_filled
    ),
    NavigationItem(
        title = "About",
        icon = R.drawable.baseline_account_circle_24
    ),
)