package com.hawk.products.data.datasources.remote.interfaces

import com.hawk.products.data.dto.ProductDto
import kotlinx.coroutines.flow.Flow

interface ProductsRemoteDataSource {
    fun getProducts(): Flow<Result<List<ProductDto>>>
}
