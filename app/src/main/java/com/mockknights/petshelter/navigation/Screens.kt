package com.mockknights.petshelter.navigation

import com.mockknights.petshelter.navigation.Screens.Detail.ARG_ID

sealed class Screens(val route: String) {
    object Welcome: Screens(WELCOME_ROUTE)
    object Login: Screens(LOGIN_ROUTE)
    object Map: Screens(MAP_ROUTE)
    object Register: Screens(REGISTER_ROUTE)
    object Detail: Screens(DETAIL_ROUTE) {
        const val ARG_ID = "id"
        fun getRoute(id: String): String {
            return DETAIL_ROUTE_PLACEHOLDER.format(id)
        }
    }



    companion object {
        private const val WELCOME_ROUTE = "welcome"
        private const val LOGIN_ROUTE = "welcome/login"
        private const val MAP_ROUTE = "welcome/map"
        private const val REGISTER_ROUTE = "login/register"
        private const val DETAIL_ROUTE = "login/detail/{$ARG_ID}"
        private const val DETAIL_ROUTE_PLACEHOLDER = "login/detail/%s"

    }
}