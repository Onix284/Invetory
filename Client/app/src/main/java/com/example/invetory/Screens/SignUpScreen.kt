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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.invetory.MyViewModels.AuthViewModels
import com.example.invetory.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel : AuthViewModels = hiltViewModel()
){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        //Values to pass in backend
        val name = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val shopName = remember { mutableStateOf("") }


        var passwordVisible by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()
        val context = LocalContext.current

        val signupState = viewModel.signupState
        val isLoading = viewModel.isLoading

        var isSubmitted by remember { mutableStateOf(false) }

        //Validation Regex
        val nameRegex = Regex("^[A-Za-z ]{2,50}$") // Name: Only letters and spaces, 2–50 characters
        val emailRegex = Regex("^[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") // Email: Standard email format
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&]).{6,}$") // Password: Min 8 chars, must include uppercase, lowercase, number, and special character
        val shopNameRegex = Regex("^[A-Za-z0-9 &.\\-]{2,100}$") // Shop Name: Letters, numbers, space, &, ., -, 2–100 characters

        val nameValid = nameRegex.matches(name.value.trim())
        val emailValid = emailRegex.matches(email.value.trim())
        val passwordValid = passwordRegex.matches(password.value)
        val shopNameValid = shopNameRegex.matches(shopName.value.trim())

        // 2️⃣ A single flag that represents a fully valid form
        val isFormValid =
                nameValid &&
                emailValid &&
                passwordValid &&
                shopNameValid

        val isFormEmpty =
                name.value.isNotBlank() &&
                email.value.isNotBlank() &&
                password.value.isNotBlank() &&
                shopName.value.isNotBlank()

        /* -----------------------React to signup state ------------------------- */
        LaunchedEffect(signupState) {
            signupState?.let {
                response ->
                    if(response.success){
                        navController.navigate(Screen.Login.route){
                            popUpTo(Screen.Signup.route) {inclusive = true}
                        }
                        Toast.makeText(context, "Signed up successfully", Toast.LENGTH_SHORT).show()
                    }
                viewModel.clearSignupState()
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
                        Text(text = "Sign Up", fontWeight = FontWeight.Bold, fontSize = 30.sp,
                            modifier = Modifier.align(alignment = Alignment.CenterStart))
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Please signup before get started.", fontWeight = FontWeight.Light, fontSize = 17.sp)

                Spacer(modifier = Modifier.height(25.dp))

                //Name TextField
                Text(
                    text = "Name",// or any style you prefer
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontSize = 20.sp
                )

                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it
                        Log.d("TXT‑DEBUG", "NAME raw → [$it]")},

                    placeholder = {Text("Enter Your Name")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isSubmitted && !nameValid,
                    supportingText = {
                        if (isSubmitted && !nameValid)
                            Text("Name should contain more than two letters only letters and spaces", color = Color.Red)
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))

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
                    singleLine = true,
                    isError = isSubmitted && !emailValid,
                    supportingText = {
                        if(isSubmitted && !emailValid){
                            Text("Enter Correct Email", color = Color.Red)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))

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
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility

                        IconButton(onClick = {passwordVisible = !passwordVisible}) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isSubmitted && !passwordValid,
                    supportingText = {
                        if(isSubmitted && !passwordValid){
                            Text("Password must contain At least one capital letter, number and special character", color = Color.Red)
                        }
                    }
                )


                Spacer(modifier = Modifier.height(15.dp))

                //Shop Name TextField
                Text(
                    text = "Shop Name",
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontSize = 20.sp,
                )

                OutlinedTextField(
                    value = shopName.value,
                    onValueChange = {shopName.value = it },
                    placeholder = {Text("Enter Your Password")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isSubmitted && !shopNameValid,
                    supportingText = {
                        if(isSubmitted && !shopNameValid){
                            Text("Shop name only contain letters and spaces", color = Color.Red)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(50.dp))

                //Signup Button
                FilledTonalButton(onClick = {
                    isSubmitted = true
                    if (!isFormEmpty){
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                    else if(!isFormValid)
                    {
                        Toast.makeText(context, "Please enter correct details", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        viewModel.signup(name.value, email.value, password.value, shopName.value)
                    }
                },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp)
                        .height(50.dp)){
                    Text(if (isLoading) "Signing up..." else "Signup", fontSize = 15.sp)
                }

                signupState?.let {
                        if(!it.success){
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            Log.d("SignupError", it.message)
                        }
                }

                Spacer(modifier = Modifier.height(25.dp))

                Box(modifier = Modifier.fillMaxWidth()){
                    //Login Button
                    Row(modifier = Modifier.align(alignment = Alignment.Center)){
                        Text(text = "Already have an account?", fontWeight = FontWeight.Light)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Login Now",
                            fontWeight = FontWeight.Medium, color = Color.Blue,
                            modifier = Modifier.clickable(onClick =
                                {navController.navigate(Screen.Login.route)}))
                    }
                }
            }
        }
    }
}