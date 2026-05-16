package com.zivansabilli3153.laundryku.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zivansabilli3153.laundryku.ui.screen.AboutScreen
import com.zivansabilli3153.laundryku.ui.screen.DetailScreen
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
                },
                onAddClick = {
                    navController.navigate(Screen.FormTambah.route)
                },
                onItemClick = { id ->
                    navController.navigate(Screen.FormUbah.withId(id))
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

        composable(Screen.FormTambah.route) {
            DetailScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onSaveClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_PESANAN) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val idPesanan = backStackEntry.arguments?.getLong(KEY_ID_PESANAN)

            DetailScreen(
                idPesanan = idPesanan,
                onBackClick = {
                    navController.navigateUp()
                },
                onSaveClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}