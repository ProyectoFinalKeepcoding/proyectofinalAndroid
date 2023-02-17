package com.mockknights.petshelter.navigation

sealed class Screens(val route: String) {
    object Welcome: Screens(WELCOME_ROUTE)
    object Login: Screens(LOGIN_ROUTE)
    object Map: Screens(MAP_ROUTE)
    object Register: Screens(REGISTER_ROUTE)
    object PetShelter: Screens(PETSHELTER_ROUTE)



    companion object {
        private const val WELCOME_ROUTE = "welcome"
        private const val LOGIN_ROUTE = "welcome/login"
        private const val MAP_ROUTE = "welcome/map"
        private const val REGISTER_ROUTE = "login/register"
        private const val PETSHELTER_ROUTE = "login/petshelter"
    }
}