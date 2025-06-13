package com.example.invetory.Network.ServiceAPIs

import com.example.invetory.Network.KtorClient
import com.example.invetory.model.SignUpResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

object AuthApiService {

    private val client : HttpClient = KtorClient.client

    val baseUrl = "http://10.0.2.2:3000"

    suspend fun signup(
        name : String,
        email : String,
        password : String,
        shop_name : String) : SignUpResponse {

        return client.post("$baseUrl/auth/signup"){
            setBody(
                mapOf("name" to name, "email" to email, "password" to password, "shop_name" to shop_name)
            )
        }.body()
    }
}