package com.example.invetory.model.AuthModels

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val name : String,
    val email : String,
    val password : String,
    val shop_name : String
)

@Serializable
data class SignUpResponse(
    val success : Boolean,
    val message : String
)