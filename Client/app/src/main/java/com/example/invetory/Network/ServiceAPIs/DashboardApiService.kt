package com.example.invetory.Network.ServiceAPIs

import android.util.Log
import com.example.invetory.Constants
import com.example.invetory.model.DashBoardModel.AddProductRequest
import com.example.invetory.model.DashBoardModel.AddProductResponse
import com.example.invetory.model.DashBoardModel.AddProductUnitRequest
import com.example.invetory.model.DashBoardModel.AddProductUnitResponse
import com.example.invetory.model.DashBoardModel.AllProductsListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class DashboardApiService @Inject constructor(
    private val client : HttpClient
) {
    private val baseUrl = Constants.BASE_URL

    suspend fun getAllProducts(user_id : Int?) : AllProductsListResponse{
        return client.get("$baseUrl/product/user/$user_id").body()
    }

    suspend fun addNewProduct(request: AddProductRequest) : AddProductResponse{
        return client.post("$baseUrl/product/add"){
            setBody(request)
        }.body()
    }

    suspend fun addNewProductUnit(request: AddProductUnitRequest) : AddProductUnitResponse{
        return client.post("$baseUrl/productUnits/add"){
            setBody(request)
        }.body()
    }
}