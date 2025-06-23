package com.example.invetory.MyViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.invetory.Network.ServiceAPIs.DashboardApiService
import com.example.invetory.model.DashBoardModel.AddProductRequest
import com.example.invetory.model.DashBoardModel.AddProductUnitRequest
import com.example.invetory.model.DashBoardModel.AddProductUnitResponse
import com.example.invetory.model.DashBoardModel.ProductData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val dashboardApiService: DashboardApiService
) : ViewModel(
) {

    private val _products = MutableStateFlow<List<ProductData>>(emptyList())
    val products : StateFlow<List<ProductData>> = _products

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _productUnitsResponse = MutableStateFlow<AddProductUnitResponse?>(null)
    val productUnitsResponse : StateFlow<AddProductUnitResponse?> = _productUnitsResponse

    fun fetchAllProducts(user_id: Int?){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = dashboardApiService.getAllProducts(user_id)
                Log.d("DashboardVM", "Fetched Products: ${response.products}")
                _products.value = response.products
                _error.value = null
            }
            catch (e : Exception){
                Log.e("DashboardVM", "Error fetching products: ${e.message}")
                _error.value = e.message ?: "Unknown error"
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun addNewProduct(productRequest: AddProductRequest){
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = dashboardApiService.addNewProduct(productRequest)

                if(response.success){
                    fetchAllProducts(productRequest.user_id)
                    Log.d("ProductUnit", "addNewProduct: ${response.product?.id} ")
                }
            }
            catch (e : Exception){
                _error.value = e.message
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun addProductUnits(productId: Int, serials: List<String>){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = AddProductUnitRequest(
                    productId,
                    serials
                )
                val response = dashboardApiService.addNewProductUnit(request)
                _productUnitsResponse.value = response
            }
            catch (e : Exception){
                Log.e("AddProductUnitVM", "API error", e)
                _productUnitsResponse.value = AddProductUnitResponse(
                    success = false,
                    message = "Something went wrong",
                    inserted = null
                )
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun clearUnitResponse() {
        _productUnitsResponse.value = null
    }
}