package com.example.invetory.Screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.invetory.MyViewModels.AuthViewModels

@Composable
fun UserDashboardScreen(authViewModel: AuthViewModels){
    val user = authViewModel.loggedInUser.collectAsState().value
    val response = authViewModel.loginResponse.collectAsState().value

    Log.d("Home", "UserDashboardScreen: $user")
    Log.d("Home", "UserDashboardScreen: $response")

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ){
        if (user != null) {
            Text("Welcome, ${user.name}")
        } else {
            Text("Loading...")
        }
    }
}
