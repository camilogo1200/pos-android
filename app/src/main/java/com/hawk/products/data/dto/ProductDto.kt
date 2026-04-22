package com.hawk.products.data.dto

data class ProductDto(
    val productId: String,
    val sku: String,
    val name: String,
    val status: String,
    val sellPriceAmount: Long,
    val costAmount: Long,
    val currency: String,
    val availableQuantity: Int
)
