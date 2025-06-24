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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.invetory.model.DashBoardModel.ProductUpdateRequest
import com.example.invetory.navigation.Screen
import com.example.invetory.ui.theme.fontFamily
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

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


    //Drawer state
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //Add product dialog form
    val context = LocalContext.current

    var isFormOpen by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<ProductData?>(null) }

    //Search
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = products.filter {
        it.type.equals(selectedCategory, ignoreCase = true) &&
                (it.company.contains(searchQuery, ignoreCase = true) ||
                    it.model_name.contains(searchQuery, ignoreCase = true)
                        )
    }

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp, horizontal = 5.dp)
                        ) {
                            //Menu icon
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(alignment = Alignment.TopStart)
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
                                    modifier = Modifier
                                        .align(alignment = Alignment.CenterHorizontally)
                                        .padding(top = 20.dp)
                                )
                                Text(
                                    text = user.shop_name,
                                    fontSize = 20.sp,
                                    fontFamily = fontFamily,
                                    modifier = Modifier
                                        .align(alignment = Alignment.CenterHorizontally)
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                                .padding(5.dp)
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


                SimpleSearchBar(
                    query = searchQuery,
                    onQueryChange = {searchQuery = it},
                    modifier = Modifier.padding(horizontal = 15.dp)
                )

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)){  }

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
                            items(filteredProducts) { product ->
                                ProductCard(productData = product,
                                    onEditClick = {
                                        productToEdit = product
                                        isFormOpen = true
                                    },
                                    onDeleteClick = {
                                        dashBoardViewModel.deleteProduct(product.id, product.user_id)
                                    }
                                )
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

            if (isFormOpen && user != null) {
                AddProductForm(
                    context = context,
                    onDismiss = { isFormOpen = false
                                productToEdit = null},
                    onAddProduct = { request, isEdit, productId ->
                        if (isEdit && productId != -1) {

                            val updateRequest = ProductUpdateRequest(
                                type = request.type,
                                company = request.company,
                                model_name = request.model_name,
                                months_of_warranty = request.months_of_warranty,
                                purchase_date = request.purchase_date,
                                price = request.price,
                                quantity = request.quantity
                            )

                            dashBoardViewModel.updateProduct(
                                productId =  productId,
                                request =  updateRequest,
                                user_id =  user.id)

                        } else {

                            val request = AddProductRequest(
                                user_id = user.id,
                                type = request.type,
                                company = request.company,
                                model_name = request.model_name,
                                months_of_warranty = request.months_of_warranty,
                                purchase_date = request.purchase_date,
                                price = request.price,
                                quantity = request.quantity
                            )

                            dashBoardViewModel.addNewProduct(request) {
                                addedProduct ->
                                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                            }
                        }
                        isFormOpen = false
                        productToEdit = null
                    },
                    initialProduct = productToEdit
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    productData: ProductData,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        colors = CardDefaults.cardColors(Color.Magenta)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(150.dp)
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
                    Text(
                        text = "Model : ${productData.model_name} ",
                        fontFamily = fontFamily,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .align(alignment = Alignment.End)
                            .padding(5.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                ) {
                    Box(modifier = Modifier.fillMaxHeight()){
                        Text(
                            text = productData.price + "₹",
                            fontFamily = fontFamily,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .align(alignment = Alignment.TopEnd)
                                .padding(10.dp)
                        )

                        Row(modifier = Modifier.align(Alignment.BottomEnd)){
                            IconButton(
                                onClick = onEditClick,
                                modifier = Modifier
                                    .align(Alignment.Bottom)
                                    .padding(end = 20.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                )
                            }

                            IconButton(
                                onClick = onDeleteClick,
                                modifier = Modifier
                                    .align(Alignment.Bottom)
                                    .padding(end = 5.dp)
                            ){
                                Icon(Icons.Default.Delete,
                                contentDescription = null,
                                )
                            }
                        }
                    }
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
    onAddProduct : (ProductUpdateRequest, isEdit : Boolean, productId : Int) -> Unit,
    initialProduct: ProductData? = null
){

    val isEdit = initialProduct != null


    var type by remember { mutableStateOf(initialProduct?.type ?: "Battery") }
    var company by remember { mutableStateOf(initialProduct?.company ?: "") }
    var modelName by remember { mutableStateOf(initialProduct?.model_name ?: "") }
    var warranty by remember { mutableStateOf(initialProduct?.months_of_warranty?.toString() ?: "") }
    var purchaseDate by remember { mutableStateOf(initialProduct?.purchase_date?.let { cleanDate(it) }) }
    var price by remember { mutableStateOf(initialProduct?.price ?: "") }
    var quantity by remember { mutableIntStateOf(initialProduct?.quantity ?: 1) }


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
                OutlinedTextField(value = purchaseDate.toString(), onValueChange = { purchaseDate = it }, label = { Text("Purchase Date") }, placeholder = {Text("YYYY/MM/DD")})
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
                OutlinedTextField(value = quantity.toString(), onValueChange = { quantity = it.toIntOrNull() ?: 0 }, label = { Text("Quantity") })

            }
        },
        confirmButton = {
            Button(onClick = {
                val trimmedCompany = company.trim()
                val trimmedModel = modelName.trim()
                val trimmedWarranty = warranty.trim()
                val trimmedDate = purchaseDate!!.trim()
                val trimmedPrice = price.trim()

                if(!isValidDate(trimmedDate)){
                    Toast.makeText(context, "Enter valid date", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                when {
                    !companyRegex.matches(trimmedCompany) ->
                        Toast.makeText(context, "Invalid company name", Toast.LENGTH_SHORT).show()
                    !modelRegex.matches(trimmedModel) ->
                        Toast.makeText(context, "Invalid model name", Toast.LENGTH_SHORT).show()
                    !warrantyRegex.matches(trimmedWarranty) ->
                        Toast.makeText(context, "Enter valid warranty in months", Toast.LENGTH_SHORT).show()
                    !priceRegex.matches(trimmedPrice) ->
                        Toast.makeText(context, "Enter valid price", Toast.LENGTH_SHORT).show()
                    !dateRegex.matches(trimmedDate) ->
                        Toast.makeText(context, "Enter date in YYYY/MM/DD format", Toast.LENGTH_SHORT).show()
                    quantity <= 0 ->
                        Toast.makeText(context, "Quantity must be at least 1", Toast.LENGTH_SHORT).show()
                    else -> {
                        val request = ProductUpdateRequest(
                            type = type,
                            company = trimmedCompany,
                            model_name = trimmedModel,
                            months_of_warranty = trimmedWarranty.toIntOrNull() ?: 0,
                            purchase_date = trimmedDate,
                            price = trimmedPrice.toDoubleOrNull() ?: 0.0,
                            quantity = quantity
                        )
                        onAddProduct(request, isEdit, initialProduct?.id ?: -1)
                        onDismiss()  // ✅ Only if all validations pass
                    }
                }
            }) {
                Text(if (isEdit) "Update" else "Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SimpleSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search") },
        singleLine = true,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = modifier.fillMaxWidth()
    )
}

fun isValidDate(input : String) : Boolean {
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        formatter.isLenient = false
    return try {
        formatter.parse(input)
        true
    }
    catch (ex : Exception){
        false
    }
}

fun cleanDate(dateTime: String): String {
    return dateTime.split("T").firstOrNull() ?: dateTime
}
