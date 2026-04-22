package com.hawk.products.data.mappers

import com.hawk.products.data.dto.ProductDto
import com.hawk.products.domain.entities.Product
import com.hawk.products.domain.entities.ProductStatus
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

fun JsonArray.toProductDtos(): List<ProductDto> = map { element ->
    val product = element.jsonObject
    val pricing = product.getValue("pricing").jsonObject
    val sellPrice = pricing.getValue("sellPrice").jsonObject
    val cost = pricing.getValue("cost").jsonObject
    val inventory = product.getValue("inventory").jsonObject

    ProductDto(
        productId = product.getValue("productId").jsonPrimitive.content,
        sku = product.getValue("sku").jsonPrimitive.content,
        name = product.getValue("name").jsonPrimitive.content,
        status = product.getValue("status").jsonPrimitive.content,
        sellPriceAmount = sellPrice.getValue("amount").jsonPrimitive.longOrNull ?: 0L,
        costAmount = cost.getValue("amount").jsonPrimitive.longOrNull ?: 0L,
        currency = sellPrice.get("currency")?.jsonPrimitive?.contentOrNull ?: "COP",
        availableQuantity = inventory.getValue("availableQuantity").jsonPrimitive.intOrNull ?: 0
    )
}

fun ProductDto.toDomain(): Product = Product(
    productId = productId,
    sku = sku,
    name = name,
    status = status.toProductStatus(),
    sellPriceAmount = sellPriceAmount,
    costAmount = costAmount,
    currency = currency,
    availableQuantity = availableQuantity
)

private fun String.toProductStatus(): ProductStatus = when (uppercase()) {
    "ACTIVE" -> ProductStatus.ACTIVE
    "INACTIVE" -> ProductStatus.INACTIVE
    "DRAFT" -> ProductStatus.DRAFT
    "ARCHIVED" -> ProductStatus.ARCHIVED
    else -> ProductStatus.UNKNOWN
}
