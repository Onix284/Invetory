package com.example.invetory.model.DashBoardModel

import kotlinx.serialization.Serializable

@Serializable
data class AllProductsListResponse(
    val success : Boolean,
    val products : List<ProductData> = emptyList()
)

@Serializable
data class ProductData(
    val id : Int,
    val user_id : Int,
    val type : String,
    val company : String,
    val model_name : String,
    val months_of_warranty : Int,
    val purchase_date : String,
    val price : String
)