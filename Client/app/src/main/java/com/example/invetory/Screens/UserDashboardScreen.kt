package com.example.invetory.Screens

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
    val userName = viewModels.loggedInUser.value?.name
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Hey $userName")
    }
}