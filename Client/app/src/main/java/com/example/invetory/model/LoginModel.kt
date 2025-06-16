package com.example.invetory.model

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(val email : String)

@Serializable
data class ForgotPasswordResponse(
    val success : Boolean,
    val message : String
)