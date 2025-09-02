package com.example.productcardlist.model

data class Root(
    val carts: List<Cart>,
    val total: Long,
    val skip: Long,
    val limit: Long,
)

data class Cart(
    val id: Long,
    val products: List<Product>,
    val total: Double,
    val discountedTotal: Double,
    val userId: Long,
    val totalProducts: Long,
    val totalQuantity: Long,
)

data class Product(
    val id: Long,
    val title: String,
    val price: Double,
    val quantity: Long,
    val total: Double,
    val discountPercentage: Double,
    val discountedTotal: Double,
    val thumbnail: String,
)
