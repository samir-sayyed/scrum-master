package com.example.dailyscrum.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dailyscrum.ui.home.*


@Composable
fun Navigation(dailyScrumViewModel: DailyScrumViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationItem.HomeScreen.route){
        composable(NavigationItem.HomeScreen.route){
            HomeScreen(navController = navController, dailyScrumViewModel = dailyScrumViewModel)
        }

        composable(NavigationItem.ScrumDetailScreen.route){
            val id1 = it.arguments?.getString("id1")
            if (id1 != null) {
                ScrumDetailScreen(id = id1.toInt(),navController, dailyScrumViewModel )
            }
        }

        composable(NavigationItem.AddScrumScreen.route){
            val id3 = it.arguments?.getString("id3")
            var edit = false
            edit = it.arguments?.getString("edit") != "false"
            if (id3 != null) {
                AddScrum(id=id3.toInt(),edit = edit,navController = navController, dailyScrumViewModel=dailyScrumViewModel)
            }
        }

        composable(NavigationItem.ScrumRunningScreen.route){
            val id2 = it.arguments?.getString("id2")
            if (id2 != null) {
                ScrumRunningScreen(id= id2.toInt(), navController = navController, dailyScrumViewModel = dailyScrumViewModel)
            }
        }
    }
}