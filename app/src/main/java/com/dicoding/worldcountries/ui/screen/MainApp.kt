package com.dicoding.worldcountries.ui.screen


import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dicoding.worldcountries.R
import com.dicoding.worldcountries.data.common.Screen
import com.dicoding.worldcountries.data.common.itemNavigation
import com.dicoding.worldcountries.data.network.local.FavoriteEntity
import com.dicoding.worldcountries.data.network.remote.DataItem
import com.dicoding.worldcountries.ui.screen.about.AboutScreen
import com.dicoding.worldcountries.ui.screen.detail.DetailScreen
import com.dicoding.worldcountries.ui.screen.favorite.FavoriteScreen
import com.dicoding.worldcountries.ui.screen.favorite.FavoriteViewModel
import com.dicoding.worldcountries.ui.screen.home.HomeScreen
import com.dicoding.worldcountries.ui.screen.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun MainApp() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            if (backStackEntry?.destination?.route != Screen.Detail.route) {
                BottomBar(navController, backStackEntry)
            }
        },
        floatingActionButton = {
            FloatingButton(navController = navController, backStackEntry = backStackEntry)
        },
        floatingActionButtonPosition = if (backStackEntry?.destination?.route == Screen.Detail.route) FabPosition.End else FabPosition.Center,
        containerColor = colorResource(id = R.color.gray)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navigateToDetail = { country ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("country", country)
                    navController.navigate(Screen.Detail.route)
                })
            }
            composable(Screen.About.route) {
                AboutScreen()
            }
            composable(Screen.Detail.route) {
                val country =
                    navController.previousBackStackEntry?.savedStateHandle?.get<DataItem>("country")
                        ?: DataItem("", "", "", "", "")
                DetailScreen(country = country)
            }
            composable(Screen.Favorite.route) {
                FavoriteScreen(navigateToDetail = {
                    country ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("country", country)
                    navController.navigate(Screen.Detail.route)
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .height(64.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.green)
        ),
        title = {
            Text(
                text = "WORLD COUNTRIES",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        },
    )
}

@Composable
fun BottomBar(
    navController: NavHostController, navBackStackEntry: NavBackStackEntry?
) {
    NavigationBar(
        containerColor = colorResource(id = R.color.white)
    ) {
        itemNavigation.forEach { items ->
            val isSelected =
                items.title == (navBackStackEntry?.destination?.route ?: Screen.Home.route)
            NavigationBarItem(
                modifier = Modifier.padding(vertical = 20.dp),
                selected = isSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(id = R.color.green),
                    unselectedIconColor = colorResource(id = R.color.gray_dark),
                    indicatorColor = Color.White
                ),
                onClick = { navController.navigate(items.title) },
                icon = {
                    if (items.title != "WhiteSpace") {
                        Icon(
                            painter = painterResource(id = items.icon),
                            contentDescription = items.title,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }
            )
        }
    }
}


@Composable
fun FloatingButton(
    navController: NavController,
    backStackEntry: NavBackStackEntry?,
    viewModel: FavoriteViewModel = koinViewModel()
) {
    val isDetailScreen = backStackEntry?.destination?.route == Screen.Detail.route

    val country = navController.previousBackStackEntry?.savedStateHandle?.get<DataItem>("country")
        ?: DataItem("", "", "", "", "")

    val data = viewModel.getFavoriteByName(country.name).collectAsState(initial = FavoriteEntity("",""))

    val isFavorite = data.value?.name?.isNotBlank() ?: false

    FloatingActionButton(
        modifier = Modifier
            .offset(y = if (isDetailScreen) (-20).dp else 60.dp)
            .size(80.dp)
            .border(
                width = 4.dp,
                color = colorResource(id = R.color.green_light),
                shape = CircleShape
            ),
        shape = CircleShape,
        onClick = {
            if (isDetailScreen) {
                val fav = FavoriteEntity(
                    name = country.name,
                    flag = country.flag,
                )
                if (isFavorite) {
                    viewModel.deleteFavorite(fav)
                } else {
                    viewModel.addFavorite(fav)
                }

            } else {
                navController.navigate(Screen.Home.route)
            }
        },
        containerColor = colorResource(id = R.color.green),
        contentColor = colorResource(id = R.color.white)
    ) {
        Icon(
            painter = painterResource(id = if (isDetailScreen) R.drawable.baseline_favorite_24 else R.drawable.carbon_earth_filled),
            contentDescription = "Home",
            modifier = Modifier.size(40.dp),
            tint = colorResource(id = if(isDetailScreen) if(isFavorite)R.color.green_light else R.color.white else R.color.white)
        )
    }
}




