package com.example.invetory.MyViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.invetory.Network.ServiceAPIs.AuthApiService
import com.example.invetory.model.SignUpRequest
import com.example.invetory.model.SignUpResponse
import kotlinx.coroutines.launch

class AuthViewModels : ViewModel() {

    var signupState by mutableStateOf<SignUpResponse?>(null)
    var isLoading by mutableStateOf(false)

    fun signup(name : String, email : String, password : String, shopName : String){
        viewModelScope.launch {
            isLoading = true
            try {
                val request = SignUpRequest(name, email, password, shopName)
                val response = AuthApiService.signup(request)
                signupState = response
            }
            catch (e : Exception){
                signupState = SignUpResponse(false, "Error ${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }

    fun clearSignupState() {
        signupState = null
    }
}