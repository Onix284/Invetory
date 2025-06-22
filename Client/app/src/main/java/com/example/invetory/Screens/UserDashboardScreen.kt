package com.example.invetory.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.invetory.MyViewModels.AuthViewModels
import com.example.invetory.MyViewModels.DashBoardViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun UserDashboardScreen(
    authViewModel: AuthViewModels,
    dashBoardViewModel: DashBoardViewModel
){
    val user = authViewModel.loggedInUser.collectAsState().value
    val user_id = user?.id

    val products by dashBoardViewModel.products.collectAsState()
    val isLoading by dashBoardViewModel.isLoading.collectAsState()
    val error by dashBoardViewModel.error.collectAsState()

    val categories = listOf("Battery", "Inverter")
    var selectedCategory by remember { mutableStateOf("Battery") }
    val filterProductList = products.filter { it.type.equals(selectedCategory, ignoreCase = true) }

    LaunchedEffect(user_id) {
        if (user_id != null) {
            Log.d("UserDashboard", "Calling fetchAllProducts with user_id = $user_id")
            dashBoardViewModel.fetchAllProducts(user_id)
        }
    }

    Log.d("Home", "UserDashboardScreen: $user")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)){

        if (user != null) {
            Text("Welcome, ${user.shop_name}")
        } else {
            Text("Loading...")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly){
            categories.forEach { category ->
                Button(
                    onClick = { selectedCategory = category },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(selectedCategory == category) Color.Blue else Color.Gray
                    )
                ) {
                    Text(category)
                }
            }
        }

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: $error", color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        dashBoardViewModel.fetchAllProducts(user_id ?: return@Button)
                    }) {
                        Text("Retry")
                    }
                }
            }

            products.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No products found.")
                }
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filterProductList) { product ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text("Model: ${product.model_name}")
                            Text("Company: ${product.company}")
                            Text("Type: ${product.type}")
                            Text("Price: ₹${product.price}")
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
