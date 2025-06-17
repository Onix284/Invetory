package com.example.invetory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.invetory.MyViewModels.AuthViewModels
import com.example.invetory.Screens.LoginScreen
import com.example.invetory.Screens.SignUpScreen
import com.example.invetory.Screens.UserDashboardScreen
import com.example.invetory.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            InvetoryNavHost(navController = navController)
        }
    }
}

@Composable
fun InvetoryNavHost(navController : NavHostController){

    NavHost(
        navController = navController,
        startDestination = Screen.Signup.route
    ){
        composable(Screen.Signup.route) {
            SignUpScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Home.route) {
            UserDashboardScreen()
        }
    }
}
