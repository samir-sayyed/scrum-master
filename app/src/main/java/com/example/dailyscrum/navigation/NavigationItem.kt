package com.example.dailyscrum.navigation

sealed class NavigationItem(val route:String){

    object HomeScreen: NavigationItem("home")
    object ScrumDetailScreen: NavigationItem("scrum_detail/{id1}")
    object AddScrumScreen: NavigationItem("add_scrum/{id3}/{edit}")
    object ScrumRunningScreen: NavigationItem("running_scrum/{id2}")
}
