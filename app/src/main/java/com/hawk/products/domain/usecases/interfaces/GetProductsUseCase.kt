package com.hawk.products.domain.usecases.interfaces

import com.hawk.products.domain.entities.Product
import kotlinx.coroutines.flow.Flow

interface GetProductsUseCase {
    operator fun invoke(): Flow<Result<List<Product>>>
}
