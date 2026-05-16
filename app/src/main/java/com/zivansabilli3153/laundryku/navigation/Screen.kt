package com.zivansabilli3153.laundryku.navigation

const val KEY_ID_PESANAN = "idPesanan"

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object About : Screen("about")
    data object FormTambah : Screen("formTambah")

    data object FormUbah : Screen("formUbah/{$KEY_ID_PESANAN}") {
        fun withId(id: Long) = "formUbah/$id"
    }
}