package com.mockknights.petshelter.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mockknights.petshelter.ui.detail.DetailScreen
import com.mockknights.petshelter.ui.login.LoginScreen
import com.mockknights.petshelter.ui.map.MapScreen
import com.mockknights.petshelter.ui.register.RegisterScreen
import com.mockknights.petshelter.ui.welcome.WelcomeScreen

@Composable
fun AppNavigation () {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Welcome.route) {

        composable(Screens.Welcome.route) {
            WelcomeScreen( navigateToLogin = {
                navController.navigate(Screens.Login.route)
            }, navigateToMap = {
                navController.navigate((Screens.Map.route))
            })

        }

        composable(Screens.Login.route) {
            LoginScreen(navigateToWelcome = {
                navController.navigate(Screens.Welcome.route)
            }, navigateToRegister = {
                navController.navigate(Screens.Register.route)
            }, navigateToDetail = {
                navController.navigate(Screens.Detail.route)
            })
        }

        composable(Screens.Detail.route) {
            DetailScreen(id = "ca57ecd4-c6a0-4b9d-9c48-46fe33305214")
//            DetailScreen(id = "4e11a6a6-0a6e-41eb-a60c-7de39df32cd6")
        }

        composable(Screens.Map.route) {
            MapScreen()
        }

        composable(Screens.Register.route) {
            RegisterScreen()
        }
    }

}