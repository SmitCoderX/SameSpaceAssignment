package com.smitcoderx.assignment.samespaceassignment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smitcoderx.assignment.samespaceassignment.forYou.ForYouModel
import com.smitcoderx.assignment.samespaceassignment.forYou.ForYouViewModel
import com.smitcoderx.assignment.samespaceassignment.screens.CustomTabRow
import com.smitcoderx.assignment.samespaceassignment.screens.PlayerScreen

@Composable
fun Navigation(
    tabs: List<String>,
    forYouViewModel: ForYouViewModel,
    data: ForYouModel?
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.ForYouScreen.route) {
        composable(route = Screens.ForYouScreen.route) {
            CustomTabRow(
                tabs = tabs, forYouViewModel = forYouViewModel, navController = navController
            )
        }
        composable(
            route = Screens.PlayerScreen.route + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                defaultValue = 0
                nullable = false
            })
        ) {
            PlayerScreen(
                viewModel = forYouViewModel,
                data = data,
                id = it.arguments?.getInt("id")
            )
        }
    }
}