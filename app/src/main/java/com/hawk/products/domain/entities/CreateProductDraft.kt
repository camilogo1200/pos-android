package com.hawk.products.domain.entities

data class CreateProductDraft(
    val productId: String,
    val sku: String,
    val name: String,
    val status: ProductStatus,
    val sellPriceAmount: Long,
    val costAmount: Long,
    val currency: String,
    val availableQuantity: Int
)
