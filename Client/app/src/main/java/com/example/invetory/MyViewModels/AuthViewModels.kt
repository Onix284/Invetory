package com.example.invetory.MyViewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.invetory.Network.ServiceAPIs.AuthApiService
import com.example.invetory.UserCredentialsStore
import com.example.invetory.model.AuthModels.ForgotPasswordRequest
import com.example.invetory.model.AuthModels.ForgotPasswordResponse
import com.example.invetory.model.AuthModels.LoginRequest
import com.example.invetory.model.AuthModels.LoginResponse
import com.example.invetory.model.AuthModels.SignUpRequest
import com.example.invetory.model.AuthModels.SignUpResponse
import com.example.invetory.model.AuthModels.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModels @Inject constructor(
    private val authApiService : AuthApiService
) : ViewModel() {

    private var _signupState = mutableStateOf<SignUpResponse?>(null)
    val signupState: State<SignUpResponse?> = _signupState

    var isLoading by mutableStateOf(false)

    private var _forgotPasswordResponse = mutableStateOf<ForgotPasswordResponse?>(null)
    val forgotPasswordResponse : State<ForgotPasswordResponse?> = _forgotPasswordResponse

    private var _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse : StateFlow<LoginResponse?> = _loginResponse

    private val _loggedInUser = MutableStateFlow<UserData?>(null)
    val loggedInUser: StateFlow<UserData?> = _loggedInUser

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


    fun login(email : String, password : String, context: Context){
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = authApiService.login(request)
                _loginResponse.value = response
                Log.d("LoginResponse", "login: ${loginResponse.value}")

                    //Save Credentials To Datastore
                    response.user?.let {
                        UserCredentialsStore.saveCredentials(
                            context,
                            response.user.id,
                            email,
                            password
                        )
                    }

                if(response.success){
                    _loggedInUser.value = response.user
                    Log.d("LoginResponse", "user: ${loggedInUser.value}")
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