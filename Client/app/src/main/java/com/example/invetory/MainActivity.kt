package com.example.invetory

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.invetory.MyViewModels.AuthViewModels
import com.example.invetory.Screens.LoginScreen
import com.example.invetory.Screens.SignUpScreen
import com.example.invetory.Screens.TestScreen
import com.example.invetory.Screens.UserDashboardScreen
import com.example.invetory.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModel : AuthViewModels = hiltViewModel()
            val context = LocalContext.current

            var startDestination by remember { mutableStateOf(Screen.Splash.route) }

            // Trigger auto login
            LaunchedEffect(Unit) {
                startDestination = withContext(Dispatchers.IO){
                    val email    = UserCredentialsStore.useEmail(context)
                    val password = UserCredentialsStore.getPassword(context)

                    if (!email.isNullOrBlank() && !password.isNullOrBlank()){
                        authViewModel.autoLogin(context)
                        Screen.Home.route
                    }
                    else{
                        Screen.Signup.route
                    }
                }
            }

            startDestination?.let {
                firstRoute ->
                InvetoryNavHost(navController, firstRoute)
            }
        }
    }
}

@Composable
fun InvetoryNavHost(navController : NavHostController,
                    startDestination : String){

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Screen.Splash.route){
            TestScreen()
        }
        composable(Screen.Signup.route) {
            SignUpScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Home.route) { backStackEntry ->
            val authViewModel = hiltViewModel<AuthViewModels>()
            UserDashboardScreen(authViewModel)
        }
    }
}
