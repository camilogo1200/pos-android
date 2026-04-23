package com.hawk.products.data.mappers

import com.hawk.products.data.dto.CreateProductInventoryDto
import com.hawk.products.data.dto.CreateProductMoneyDto
import com.hawk.products.data.dto.CreateProductPricingDto
import com.hawk.products.data.dto.CreateProductRequestDto
import com.hawk.products.data.dto.ProductDto
import com.hawk.products.domain.entities.CreateProductDraft
import com.hawk.products.domain.entities.Product
import com.hawk.products.domain.entities.ProductStatus

fun ProductDto.toDomain(): Product = Product(
    productId = productId,
    sku = sku,
    name = name,
    status = status.toProductStatus(),
    sellPriceAmount = pricing.sellPrice.amount,
    costAmount = pricing.cost.amount,
    currency = pricing.sellPrice.currency,
    availableQuantity = inventory.availableQuantity
)

fun CreateProductDraft.toCreateProductRequestDto(): CreateProductRequestDto = CreateProductRequestDto(
    productId = productId,
    sku = sku,
    name = name,
    status = status.toTransportValue(),
    pricing = CreateProductPricingDto(
        sellPrice = CreateProductMoneyDto(
            amount = sellPriceAmount,
            currency = currency
        ),
        cost = CreateProductMoneyDto(
            amount = costAmount,
            currency = currency
        )
    ),
    inventory = CreateProductInventoryDto(
        availableQuantity = availableQuantity
    )
)

private fun String.toProductStatus(): ProductStatus = when (uppercase()) {
    "ACTIVE" -> ProductStatus.ACTIVE
    "INACTIVE" -> ProductStatus.INACTIVE
    "DRAFT" -> ProductStatus.DRAFT
    "ARCHIVED" -> ProductStatus.ARCHIVED
    else -> ProductStatus.UNKNOWN
}

private fun ProductStatus.toTransportValue(): String = when (this) {
    ProductStatus.ACTIVE -> "ACTIVE"
    ProductStatus.INACTIVE -> "INACTIVE"
    ProductStatus.DRAFT -> "DRAFT"
    ProductStatus.ARCHIVED -> "ARCHIVED"
    ProductStatus.UNKNOWN -> "UNKNOWN"
}
