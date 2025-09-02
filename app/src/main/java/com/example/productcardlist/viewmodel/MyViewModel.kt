package com.example.productcardlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productcardlist.RetrofitClient
import com.example.productcardlist.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyViewModel:ViewModel() {
    private val _carts = MutableStateFlow<Root?>(null)
    val carts=_carts.asStateFlow()

    init {
        viewModelScope.launch {
            getCarts()
        }
    }
    private suspend fun getCarts(){
        _carts.value = RetrofitClient.cartListAPIService.getCartList()
    }
    fun findCartItemById(itemId: Long): Cart? {
        return carts.value?.carts?.firstOrNull{ it.id==itemId }
    }
}