package com.example.invetory.Network.ServiceAPIs

import com.example.invetory.Network.KtorClient
import com.example.invetory.model.SignUpRequest
import com.example.invetory.model.SignUpResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

object AuthApiService {

    private val client : HttpClient = KtorClient.client

    private const val baseUrl = "http://10.0.2.2:3000"

    suspend fun signup(request: SignUpRequest)
    : SignUpResponse =
        client.post("$baseUrl/auth/signup"){
        setBody(request)
    }.body()
}