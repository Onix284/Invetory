package com.example.invetory.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.invetory.MyViewModels.AuthViewModels
import com.example.invetory.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel : AuthViewModels = hiltViewModel()
){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        //Values to pass in backend
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        val scrollState = rememberScrollState()
        val context = LocalContext.current

        var showDialog by remember { mutableStateOf(false) }
        var passwordVisible by remember { mutableStateOf(false) }

        val forgotPasswordResponse by viewModel.forgotPasswordResponse
        val loginResponse by viewModel.loginResponse


        LaunchedEffect(forgotPasswordResponse) {
            forgotPasswordResponse?.let {
                response ->
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()

                if(response.success){
                    showDialog = false
                }

                viewModel.clearForgotPasswordResponse()
            }
        }

        LaunchedEffect(loginResponse) {
            loginResponse?.let {
                response ->
                if(response.success){
                    navController.navigate(Screen.Home.route)
                    Log.d("LoginResponse", "LoginScreen: ${viewModel.loggedInUser.value}")
                }else{
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }

                viewModel.clearLoginResponse()
            }
        }



        //Main Parent
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .padding(top = 30.dp)){

            //Elements Columns
            Column(modifier = Modifier
                .padding(5.dp)
                .padding(top = 10.dp)
                .verticalScroll(scrollState)
            ){
                //Header
                Row{
                    Box(modifier = Modifier.fillMaxWidth()){
                        Text(text = "Log in", fontWeight = FontWeight.Bold, fontSize = 30.sp,
                            modifier = Modifier.align(alignment = Alignment.CenterStart))
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Get started now.", fontWeight = FontWeight.Light, fontSize = 17.sp)

                Spacer(modifier = Modifier.height(55.dp))

                //Email TextField
                Text(
                    text = "Email",// or any style you prefer
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontSize = 20.sp
                )

                OutlinedTextField(
                    value = email.value,
                    onValueChange = {email.value = it },
                    placeholder = {Text("Enter Your Email")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(25.dp))

                //Password TextField
                Text(
                    text = "Password",// or any style you prefer
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontSize = 20.sp,
                )

                OutlinedTextField(
                    value = password.value,
                    onValueChange = {password.value = it },
                    placeholder = {Text("Enter Your Password")},
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {

                        val icon = if(passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility

                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                //Forgot Password
                Text(
                    text = "Forget Password",// or any style you prefer
                    fontSize = 13.sp,
                    color = Color.Blue,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .clickable(onClick = {
                            showDialog = true
                        })
                )

                if(showDialog){
                    ForgotPasswordDialog(
                        onDismiss = {
                            showDialog = false
                            Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()},
                        onSend = {
                            email -> viewModel.forgotPassword(email)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                //Login Button
                FilledTonalButton(onClick = {
                    viewModel.login(email.value, password.value, context)
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp)
                        .height(50.dp)){
                    Text("Login", fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(25.dp))

                Box(modifier = Modifier.fillMaxWidth()){
                    //Signup Button Text
                    Row(modifier = Modifier.align(alignment = Alignment.Center)){
                        Text(text = "Don’t have an account?", fontWeight = FontWeight.Light)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Signup Now",
                            fontWeight = FontWeight.Medium,
                            color = Color.Blue,
                            modifier = Modifier.clickable(
                                onClick = {
                                    navController.navigate(Screen.Signup.route)
                                }
                            ))
                    }
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordDialog(
    onDismiss : () -> Unit,
    onSend : (String) -> Unit
){
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Forget Password")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    label = {Text("Enter Your Email")},
                    singleLine = true
                )
            }
        },
        confirmButton = {
            OutlinedButton(onClick = {
                onSend(email)
            }){
                Text("Send")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = {
                     onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}