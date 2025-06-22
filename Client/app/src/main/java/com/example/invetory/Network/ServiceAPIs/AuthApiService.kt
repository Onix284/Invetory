package com.example.invetory.Network.ServiceAPIs

import com.example.invetory.Constants
import com.example.invetory.model.AuthModels.ForgotPasswordRequest
import com.example.invetory.model.AuthModels.ForgotPasswordResponse
import com.example.invetory.model.AuthModels.LoginRequest
import com.example.invetory.model.AuthModels.LoginResponse
import com.example.invetory.model.AuthModels.SignUpRequest
import com.example.invetory.model.AuthModels.SignUpResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class AuthApiService @Inject constructor(
    private val client: HttpClient
){

    private val baseUrl = Constants.BASE_URL

    suspend fun signup(request: SignUpRequest) : SignUpResponse {
        return client.post("$baseUrl/auth/signup"){
            setBody(request)
        }.body()
    }

    suspend fun forgotPassword(request: ForgotPasswordRequest) : ForgotPasswordResponse {
        return client.post("$baseUrl/auth/forgot-password"){
            setBody(request)
        }.body()
    }

    suspend fun login(request: LoginRequest) : LoginResponse{
        return client.post("$baseUrl/auth/login"){
            setBody(request)
        }.body()
    }
}