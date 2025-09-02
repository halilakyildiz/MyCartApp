package com.example.productcardlist

import com.example.productcardlist.model.Cart
import com.example.productcardlist.model.Root
import retrofit2.http.GET

interface ICartListService {

    @GET("/carts")
    suspend fun getCartList():Root
}