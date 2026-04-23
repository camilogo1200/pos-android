package com.hawk.products.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateProductRequestDto(
    val productId: String,
    val sku: String,
    val name: String,
    val status: String,
    val pricing: CreateProductPricingDto,
    val inventory: CreateProductInventoryDto
)

@Serializable
data class CreateProductPricingDto(
    val sellPrice: CreateProductMoneyDto,
    val cost: CreateProductMoneyDto
)

@Serializable
data class CreateProductMoneyDto(
    val amount: Long,
    val currency: String
)

@Serializable
data class CreateProductInventoryDto(
    val availableQuantity: Int
)
