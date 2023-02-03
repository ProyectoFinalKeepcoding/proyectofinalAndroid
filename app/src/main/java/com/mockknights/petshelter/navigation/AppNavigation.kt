package com.mockknights.petshelter.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mockknights.petshelter.ui.login.LoginScreen
import com.mockknights.petshelter.ui.map.MapScreen
import com.mockknights.petshelter.ui.welcome.WelcomeScreen

@Composable
fun AppNavigation () {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Map.route) {

        composable(Screens.Welcome.route) {
            WelcomeScreen()
        }

        composable(Screens.Login.route) {
            LoginScreen()
        }

        composable(Screens.Map.route) {
            MapScreen()
        }
    }

}