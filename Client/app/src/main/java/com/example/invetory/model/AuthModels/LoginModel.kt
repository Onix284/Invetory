package com.example.invetory.model.AuthModels

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(val email : String)

@Serializable
data class ForgotPasswordResponse(
    val success : Boolean,
    val message : String
)


@Serializable
data class LoginRequest(
    val email : String,
    val password : String
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: UserData? = null // Nullable because on login failure, there's no user
)

@Serializable
data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val shop_name: String
)