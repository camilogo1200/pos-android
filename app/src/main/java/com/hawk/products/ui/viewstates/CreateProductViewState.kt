package com.hawk.products.ui.viewstates

data class CreateProductViewState(
    val name: String = "",
    val sku: String = "",
    val sellPrice: String = "",
    val costPrice: String = "",
    val availableQuantity: String = "",
    val status: String = "ACTIVE"
)
