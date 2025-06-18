package com.example.invetory.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TestScreen(){
    Box(modifier = Modifier.fillMaxSize().background(color = Color.White)){
        Text(text = "Home Screen Test", modifier = Modifier.align(Alignment.Center))
    }
}