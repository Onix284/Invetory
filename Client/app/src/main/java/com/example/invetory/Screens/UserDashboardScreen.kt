package com.example.invetory.Screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.invetory.MyViewModels.AuthViewModels
import com.example.invetory.MyViewModels.DashBoardViewModel
import com.example.invetory.model.DashBoardModel.AddProductRequest
import com.example.invetory.model.DashBoardModel.ProductData
import com.example.invetory.navigation.Screen
import com.example.invetory.ui.theme.fontFamily
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun UserDashboardScreen(
    authViewModel: AuthViewModels,
    dashBoardViewModel: DashBoardViewModel,
    navController: NavController
) {
    //Current user
    val user = authViewModel.loggedInUser.collectAsState().value
    val user_id = user?.id

    //Products
    val products by dashBoardViewModel.products.collectAsState()
    val isLoading by dashBoardViewModel.isLoading.collectAsState()
    val error by dashBoardViewModel.error.collectAsState()

    //Sort by type
    val categories = listOf("Battery", "Inverter")
    var selectedCategory by remember { mutableStateOf("Battery") }
    val filterProductList = products.filter { it.type.equals(selectedCategory, ignoreCase = true) }

    //Drawer state
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //Add product dialog form
    var isFormOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current


    LaunchedEffect(user_id) {
        if (user_id != null) {
            dashBoardViewModel.fetchAllProducts(user_id)
        }
    }

    //Navigation drawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val drawerWidth = screenWidth * 0.7f

            //Drawer width and size
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(drawerWidth)
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                Text("Item 1", fontSize = 20.sp)

                Spacer(Modifier.height(16.dp))

                //Contents in drawer
                Text(
                    "Batteries",
                    modifier = Modifier
                        .clickable {
                            scope.launch { drawerState.close() }
                        }
                        .padding(vertical = 8.dp)
                )
                Text(
                    "Inverters",
                    modifier = Modifier
                        .clickable {
                            scope.launch { drawerState.close() }
                        }
                        .padding(vertical = 8.dp)
                )
                Text(
                    text = "Sign out",
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            navController.navigate(Screen.Login.route)
                        }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize())
        {
            Column(modifier = Modifier.fillMaxSize())
            {
                if (user != null) {
                    // Header
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 20.dp, horizontal = 5.dp)
                        ) {
                            //Menu icon
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                modifier = Modifier.align(alignment = Alignment.TopStart)
                                    .padding(top = 20.dp)
                                    .padding(start = 10.dp)
                                    .size(35.dp)
                                    .clickable {
                                        scope.launch { drawerState.open() }
                                    }
                            )

                            //Welcom text
                            Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                                Text(
                                    text = "Welcome",
                                    fontSize = 20.sp,
                                    fontFamily = fontFamily,
                                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                                        .padding(top = 20.dp)
                                )
                                Text(
                                    text = user.shop_name,
                                    fontSize = 20.sp,
                                    fontFamily = fontFamily,
                                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                                        .padding(top = 10.dp)
                                )
                            }
                        }
                    }

                } else {
                    Text("Loading...")
                }

                //Sort by type buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    categories.forEach { category ->
                        Button(
                            onClick = { selectedCategory = category },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedCategory == category) Color.Blue else Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth().weight(0.5f).padding(5.dp)
                                .shadow(2.dp),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(
                                category,
                                fontFamily = fontFamily,
                                fontSize = 15.sp,
                                color = if (selectedCategory == category) Color.White else Color.Black
                            )
                        }
                    }
                }

                //handle state
                when {
                    //if loading
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    //If error
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

                    //If no products added
                    products.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No products found.")
                        }
                    }

                    //Main list of products
                    else -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(filterProductList) { product ->
                                ProductCard(product)
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                     isFormOpen = true
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp)
                    .clip(shape = RoundedCornerShape(20.dp)),

                containerColor = Color.Blue
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product",
                    tint = Color.White
                )
            }

            if(isFormOpen && user != null){
                AddProductForm(
                    context = context,
                    onDismiss = {isFormOpen = false},
                    onAddProduct = { request ->
                        dashBoardViewModel.addNewProduct(request)
                    },
                    user_id = user.id
                )
            }
        }
    }
}

@Composable
fun ProductCard(productData: ProductData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 15.dp),
        colors = CardDefaults.cardColors(Color.Magenta)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .size(120.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = productData.company,
                        fontFamily = fontFamily,
                        fontSize = 30.sp,
                        modifier = Modifier
                            .align(alignment = Alignment.Start)
                            .padding(15.dp)
                    )
                    Text(
                        text = "Warranty : ${productData.months_of_warranty} months",
                        fontFamily = fontFamily,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .align(alignment = Alignment.End)
                            .padding(10.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                ) {
                    Text(
                        text = "Model : ${productData.model_name} ",
                        fontFamily = fontFamily,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .align(alignment = Alignment.End)
                            .padding(10.dp)
                    )
                    Text(
                        text = productData.price + "₹",
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(alignment = Alignment.End)
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductForm(
    context : Context,
    onDismiss : () -> Unit,
    onAddProduct : (AddProductRequest) -> Unit,
    user_id : Int
){
    var type by remember { mutableStateOf("Battery") }
    var company by remember { mutableStateOf("") }
    var modelName by remember { mutableStateOf("") }
    var warranty by remember { mutableStateOf("") }
    var purchaseDate by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }


    val companyRegex = Regex("^[A-Za-z0-9\\s]{2,30}$")
    val modelRegex = Regex("^[A-Za-z0-9\\- ]{2,50}$")
    val warrantyRegex = Regex("^\\d{1,2}$")
    val priceRegex = Regex("^\\d{1,7}(\\.\\d{1,2})?$")
    val dateRegex = Regex("^\\d{4}/\\d{2}/\\d{2}$")

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add New Product", fontFamily = fontFamily)
        },
        text = {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Battery", "Inverter").forEach {
                        Button(
                            onClick = { type = it },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (type == it) Color.Blue else Color.LightGray
                            )
                        ) {
                            Text(it, color = if(type == it)  Color.White else Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company") })
                OutlinedTextField(value = modelName, onValueChange = { modelName = it }, label = { Text("Model Name") })
                OutlinedTextField(value = warranty, onValueChange = { warranty = it }, label = { Text("Warranty (months)") })
                OutlinedTextField(value = purchaseDate, onValueChange = { purchaseDate = it }, label = { Text("Purchase Date") }, placeholder = {Text("YYYY/MM/DD")})
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val request = AddProductRequest(
                    user_id = user_id,
                    type = type,
                    company = company.trim(),
                    model_name = modelName.trim(),
                    months_of_warranty = warranty.toIntOrNull() ?: 0,
                    purchase_date = purchaseDate.trim(),
                    price = price.toDoubleOrNull() ?: 0.0
                )

                if (!companyRegex.matches(company.trim())) {
                    Toast.makeText(context, "Invalid company name", Toast.LENGTH_SHORT).show(); return@Button
                }
                if (!modelRegex.matches(modelName.trim())) {
                    Toast.makeText(context, "Invalid model name", Toast.LENGTH_SHORT).show(); return@Button
                }
                if (!warrantyRegex.matches(warranty.trim())) {
                    Toast.makeText(context, "Enter valid warranty in months", Toast.LENGTH_SHORT).show(); return@Button
                }
                if (!priceRegex.matches(price.trim())) {
                    Toast.makeText(context, "Enter valid price", Toast.LENGTH_SHORT).show(); return@Button
                }
                if (!dateRegex.matches(purchaseDate.trim())) {
                    Toast.makeText(context, "Enter date in YYYY/MM/DD format", Toast.LENGTH_SHORT).show(); return@Button
                }

                onAddProduct(request)
                onDismiss()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
