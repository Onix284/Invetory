package com.example.invetory.Network.ServiceAPIs

import android.util.Log
import com.example.invetory.Constants
import com.example.invetory.model.DashBoardModel.AllProductsListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class DashboardApiService @Inject constructor(
    private val client : HttpClient
) {
    private val baseUrl = Constants.BASE_URL

    suspend fun getAllProducts(user_id : Int?) : AllProductsListResponse{
        return client.get("$baseUrl/product/user/$user_id").body()
    }
}