package com.hawk.products.domain.usecases.interfaces

import com.hawk.products.domain.entities.CreateProductDraft
import kotlinx.coroutines.flow.Flow

interface CreateProductUseCase {
    operator fun invoke(product: CreateProductDraft): Flow<Result<Unit>>
}
