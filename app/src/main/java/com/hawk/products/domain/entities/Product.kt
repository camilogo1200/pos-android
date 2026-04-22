package com.hawk.products.domain.entities

data class Product(
    val productId: String,
    val sku: String,
    val name: String,
    val status: ProductStatus,
    val sellPriceAmount: Long,
    val costAmount: Long,
    val currency: String,
    val availableQuantity: Int
)

enum class ProductStatus {
    ACTIVE,
    INACTIVE,
    DRAFT,
    ARCHIVED,
    UNKNOWN
}
