package com.example.invetory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.invetory.Screens.LoginScreen
import com.example.invetory.Screens.SignUpScreen
import com.example.invetory.Screens.UserDashboardScreen
import com.example.invetory.navigation.Screen
import com.example.invetory.ui.theme.InvetoryTheme

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
