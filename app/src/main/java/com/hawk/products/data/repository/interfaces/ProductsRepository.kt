package com.hawk.products.data.repository.interfaces

import com.hawk.products.domain.entities.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProducts(): Flow<Result<List<Product>>>
}
