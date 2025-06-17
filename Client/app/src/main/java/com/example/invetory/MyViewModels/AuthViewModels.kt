package com.example.invetory.MyViewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.invetory.Network.ServiceAPIs.AuthApiService
import com.example.invetory.model.ForgotPasswordRequest
import com.example.invetory.model.ForgotPasswordResponse
import com.example.invetory.model.LoginRequest
import com.example.invetory.model.LoginResponse
import com.example.invetory.model.SignUpRequest
import com.example.invetory.model.SignUpResponse
import com.example.invetory.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModels @Inject constructor(
    private val authApiService : AuthApiService
) : ViewModel() {

    private var _signupState = mutableStateOf<SignUpResponse?>(null)
    val signupState: State<SignUpResponse?> = _signupState

    var isLoading by mutableStateOf(false)

    private var _forgotPasswordResponse = mutableStateOf<ForgotPasswordResponse?>(null)
    val forgotPasswordResponse : State<ForgotPasswordResponse?> = _forgotPasswordResponse

    private var _loginResponse = mutableStateOf<LoginResponse?>(null)
    val loginResponse : State<LoginResponse?> = _loginResponse

    private val _loggedInUser = mutableStateOf<UserData?>(null)
    val loggedInUser: State<UserData?> = _loggedInUser

    fun signup(name : String, email : String, password : String, shopName : String){
        viewModelScope.launch {
            isLoading = true
            try {
                val request = SignUpRequest(name, email, password, shopName)
                val response = authApiService.signup(request)
                _signupState.value = response
            }
            catch (e : Exception){
                _signupState.value = SignUpResponse(false, "Error ${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }

    fun clearSignupState() {
        _signupState.value = null
    }


    fun forgotPassword(email: String){
        viewModelScope.launch {
            try {
                val request = ForgotPasswordRequest(email)
                val response = authApiService.forgotPassword(request)
                _forgotPasswordResponse.value = response
            }
            catch (e : Exception){
                _forgotPasswordResponse.value = ForgotPasswordResponse(false, "Error ${e.message}")
            }
        }
    }

    fun clearForgotPasswordResponse(){
        _forgotPasswordResponse.value = null
    }


    fun login(email : String, password : String){
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = authApiService.login(request)
                _loginResponse.value = response

                if(response.success){
                    _loggedInUser.value = response.user
                }
            }
            catch (e : Exception){
                _loginResponse.value = LoginResponse(false, "Error ${e.message}")
            }
        }
    }

    fun clearLoginResponse(){
        _loginResponse.value = null
    }

}