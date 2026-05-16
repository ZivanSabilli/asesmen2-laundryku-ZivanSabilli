package com.zivansabilli3153.laundryku.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object About : Screen("about")
    data object FormTambah : Screen("formTambah")
}