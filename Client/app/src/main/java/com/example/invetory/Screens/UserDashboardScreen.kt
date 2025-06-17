package com.example.invetory.Screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.invetory.MyViewModels.AuthViewModels

@Composable
fun UserDashboardScreen(
    viewModels: AuthViewModels = hiltViewModel()
){
    val userName = viewModels.loggedInUser.value
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Hey ${userName?.name}")
        Log.d("LoginResponse", "userDashboard: $userName")
    }
}