package com.hawk.products.domain.repository.interfaces

import com.hawk.products.domain.entities.CreateProductDraft
import com.hawk.products.domain.entities.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProducts(): Flow<Result<List<Product>>>

    fun createProduct(product: CreateProductDraft): Flow<Result<Unit>>
}
