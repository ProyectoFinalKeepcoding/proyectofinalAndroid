package com.mockknights.petshelter.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            LoginScreen(
                navigateToWelcome = {
                navController.navigate(Screens.Welcome.route)
                },
                navigateToRegister = {
                navController.navigate(Screens.Register.route)
                 },
                navigateToDetail = {
                navController.navigate(Screens.Detail.getRoute(it))
                })
        }

        composable(
            Screens.Detail.route,
            arguments = listOf(navArgument(Screens.Detail.ARG_ID) {
                type = NavType.StringType
                nullable = false
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(Screens.Detail.ARG_ID)?.let { id ->
                DetailScreen(id = id, navigateToLogin = {
                    navController.navigate(Screens.Login.route)
                })
            }
        }

        composable(Screens.Map.route) {
            MapScreen()
        }

        composable(Screens.Register.route) {
            RegisterScreen(
                navigateToLogin = {
                    navController.navigate(Screens.Login.route)
                }
            )
        }
    }

}