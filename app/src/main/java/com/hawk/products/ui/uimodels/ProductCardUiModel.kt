package com.hawk.products.ui.uimodels

data class ProductCardUiModel(
    val productId: String,
    val sku: String,
    val name: String,
    val statusLabel: String,
    val statusTone: ProductStatusTone,
    val sellPriceLabel: String,
    val costPriceLabel: String,
    val stockLabel: String,
    val availableQuantity: Int
)

enum class ProductStatusTone {
    POSITIVE,
    NEUTRAL,
    WARNING
}
