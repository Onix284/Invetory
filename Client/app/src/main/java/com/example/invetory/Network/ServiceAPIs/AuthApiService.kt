package com.example.invetory.Network.ServiceAPIs

import com.example.invetory.Network.KtorClient
import com.example.invetory.model.ForgotPasswordRequest
import com.example.invetory.model.ForgotPasswordResponse
import com.example.invetory.model.LoginRequest
import com.example.invetory.model.LoginResponse
import com.example.invetory.model.SignUpRequest
import com.example.invetory.model.SignUpResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class AuthApiService @Inject constructor(
    private val client: HttpClient
){

    private val baseUrl = "http://192.168.196.129:3000"

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