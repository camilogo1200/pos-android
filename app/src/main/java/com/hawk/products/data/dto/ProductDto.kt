package com.hawk.products.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val productId: String,
    val sku: String,
    val name: String,
    val status: String,
    val pricing: ProductPricingDto,
    val inventory: ProductInventoryDto
)

@Serializable
data class ProductPricingDto(
    val sellPrice: ProductMoneyDto,
    val cost: ProductMoneyDto
)

@Serializable
data class ProductMoneyDto(
    val amount: Long,
    val currency: String
)

@Serializable
data class ProductInventoryDto(
    val availableQuantity: Int
)
