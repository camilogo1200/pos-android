package com.hawk.products.data.datasources.remote.interfaces

import com.hawk.products.data.dto.CreateProductRequestDto
import com.hawk.products.data.dto.ProductDto
import kotlinx.coroutines.flow.Flow

interface ProductsRemoteDataSource {
    fun getProducts(): Flow<Result<List<ProductDto>>>

    fun createProduct(product: CreateProductRequestDto): Flow<Result<ProductDto>>
}
