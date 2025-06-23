package com.example.invetory.model.DashBoardModel

import kotlinx.serialization.Serializable

//Get All Product List
@Serializable
data class AllProductsListResponse(
    val success : Boolean,
    val products : List<ProductData> = emptyList()
)

@Serializable
data class  ProductData(
    val id : Int,
    val user_id : Int,
    val type : String,
    val company : String,
    val model_name : String,
    val months_of_warranty : Int,
    val purchase_date : String,
    val price : String,
    val quantity: Int
)

//Add New Product
@Serializable
data class AddProductRequest(
    val user_id : Int,
    val type : String,
    val company : String,
    val model_name : String,
    val months_of_warranty : Int,
    val purchase_date : String,
    val price : Double,
    val quantity : Int
)

@Serializable
data class AddProductResponse(
    val success: Boolean,
    val message: String,
    val product : ProductData? = null,
)

