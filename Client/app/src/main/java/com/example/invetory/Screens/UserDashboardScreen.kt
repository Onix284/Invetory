package com.example.invetory.Screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import com.example.invetory.MyViewModels.AuthViewModels

@Composable
fun UserDashboardScreen(authViewModel: AuthViewModels){
    val user by authViewModel.loggedInUser.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column {
            Text(text = "Hey ${user?.name}")
            Text(text = "Hey ${user?.id}")
        }
        Log.d("AutoLogin User Dashboard", "userDashboard: ${user?.email}")
    }
}
