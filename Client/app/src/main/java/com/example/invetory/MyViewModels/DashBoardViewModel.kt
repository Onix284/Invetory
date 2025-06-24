package com.example.invetory.MyViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.invetory.Network.ServiceAPIs.DashboardApiService
import com.example.invetory.model.DashBoardModel.AddProductRequest
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

    fun addNewProduct(productRequest: AddProductRequest,
                      onSuccess : (ProductData) -> Unit
    ){
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = dashboardApiService.addNewProduct(productRequest)

                if (response.success && response.product != null)
                {
                    fetchAllProducts(productRequest.user_id)
                    onSuccess(response.product)
                }
                else {
                    _error.value = response.message
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unexpected error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProduct(productId : Int, user_id: Int?){
        _error.value = null
        viewModelScope.launch {

            try {
                val response = dashboardApiService.deleteProduct(productId)
                if(response.success){
                    _error.value = null
                    fetchAllProducts(user_id)
                }
                else{
                    _error.value = response.message
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
}