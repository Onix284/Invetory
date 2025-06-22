package com.example.invetory.navigation

sealed class Screen(val route : String){
    object Signup : Screen("signup")
    object Login : Screen("login")
    object Home : Screen("user_dashboard")
}