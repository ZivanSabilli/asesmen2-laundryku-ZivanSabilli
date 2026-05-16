package com.zivansabilli3153.laundryku.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zivansabilli3153.laundryku.ui.screen.AboutScreen
import com.zivansabilli3153.laundryku.ui.screen.MainScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            MainScreen(
                onAboutClick = {
                    navController.navigate(Screen.About.route)
                }
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}